package org.grnet.cat.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.UriInfo;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.grnet.cat.dtos.*;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.entities.User;
import org.grnet.cat.entities.Validation;
import org.grnet.cat.entities.history.History;
import org.grnet.cat.enums.MailType;
import org.grnet.cat.enums.Source;
import org.grnet.cat.enums.ValidationStatus;
import org.grnet.cat.exceptions.ConflictException;
import org.grnet.cat.mappers.UserMapper;
import org.grnet.cat.mappers.ValidationMapper;
import org.grnet.cat.repositories.*;
import org.grnet.cat.repositories.registry.RegistryActorRepository;
import org.grnet.cat.services.assessment.JsonAssessmentService;
import org.grnet.cat.utils.Utility;
import org.jboss.logging.Logger;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * The UserService provides operations for managing User entities.
 */

@ApplicationScoped
public class UserService {
    @Inject
    MailerService mailerService;
    /**
     * Injection point for the User Repository
     */
    @Inject
    UserRepository userRepository;

    /**
     * Injection point for the Validation Service
     */
    @Inject
    ValidationService validationService;

    @Inject
    ValidationRepository validationRepository;

    @Inject
    JsonAssessmentService jsonAssessmentService;

    /**
     * Injection point for the Role repository
     */
    @Inject
    @Named("keycloak-repository")
    RoleRepository roleRepository;

    /**
     * Injection point for the history repository
     */
    @Inject
    HistoryRepository historyRepository;

    @Inject
    @Named("keycloak-service")
    RoleService roleService;

    @Inject
    RegistryActorRepository registryActorRepository;

    @Inject
    MotivationAssessmentRepository motivationAssessmentRepository;

    @ConfigProperty(name = "api.cat.validations.approve.auto")
    boolean autoApprove;

    @Inject
    Utility utility;

    private static final Logger LOG = Logger.getLogger(UserService.class);

    /**
     * Get User's profile.
     *
     * @param id User's voperson_id
     * @return User's Profile.
     */
    public UserProfileDto getUserProfile(String id) {

        var userProfile = userRepository.fetchUser(id);

        return UserMapper.INSTANCE.userToProfileDto(userProfile);
    }

    /**
     * Get User's profile.
     *
     * @param userId User's voperson_id
     * @return User's profile information and validation and assessment counting.
     */
    public UserInfoDto getUserStatsById(String userId) {

        var userProfile = getUserProfile(userId);

        var info = new UserInfoDto();
        info.id = userProfile.id;
        info.name = userProfile.name;
        info.surname = userProfile.surname;
        info.email = userProfile.email;
        info.banned = userProfile.banned;
        info.orcidId = userProfile.orcidId;
        info.roles = userProfile.roles;
        info.registeredOn = userProfile.registeredOn;
        info.type = userProfile.type;
        info.validatedOn = userProfile.validatedOn;
        info.updatedOn = userProfile.updatedOn;
        info.totalValidationsCount = validationRepository.countValidationsByUserId(info.id);
        info.totalAssessmentsCount = motivationAssessmentRepository.countAllAssessmentsByUser(info.id);

        return info;
    }

    /**
     * Retrieves a page of users from the database.
     *
     * @param search  Enables clients to specify a text string for searching specific fields within User entity.
     * @param page    The index of the page to retrieve (starting from 0).
     * @param size    The maximum number of users to include in a page.
     * @param sort    Specifies the field by which the results to be sorted.
     * @param order   Specifies the order in which the sorted results should be returned.
     * @param status  Indicates whether the user is active or deleted.
     * @param type    Filters the results based on the type of user.
     * @param uriInfo The Uri Info.
     * @return A list of UserProfileDto objects representing the users in the requested page.
     */
    public PageResource<UserProfileDto> getUsersByPage(String search, String sort, String order, String status, String type, int page, int size, UriInfo uriInfo) {

        var users = userRepository.fetchUsersByPage(search, sort, order, status, type, page, size);

        return new PageResource<>(users, UserMapper.INSTANCE.usersProfileToDto(users.list()), uriInfo);
    }

    /**
     * Updates the metadata for a user's profile.
     *
     * @param id      The ID of the user whose metadata is being updated.
     * @param name    The user's name.
     * @param surname The user's surname.
     * @param email   The user's email address.
     * @param orcidId The user's orcid id.
     * @return The updated user's profile
     */
    public UserProfileDto updateUserProfileMetadata(String id, String name, String surname, String email, String orcidId) {

       Optional<User> optionalUser=userRepository.fetchUserByEmail(email);
        if (optionalUser.isPresent() && !optionalUser.get().getId().equals(id)){
            throw new ConflictException("There is a User with email : " + email);
        }
        var user = userRepository.updateUserMetadata(id, name, surname, email, orcidId);

        return UserMapper.INSTANCE.userToProfileDto(user);
    }

    /**
     * This operations registers a user on the CAT service.
     * Typically, it constructs an {@link User Identified} object and stores it in the database.
     *
     * @param id User's voperson_id
     */
    @Transactional
    public UserProfileDto register(String id) {

        var optionalUser = userRepository.searchByIdOptional(id);

        roleRepository.assignRoles(id, List.of("identified"));

        optionalUser.ifPresent(s -> {
            throw new ConflictException("User already exists in the database.");
        });

        var identified = new User();
        identified.setId(id);
        identified.setRegisteredOn(Timestamp.from(Instant.now()));
        identified.setBanned(Boolean.FALSE);

        userRepository.persist(identified);

        return UserMapper.INSTANCE.userToProfileDto(identified);
    }

    /**
     * Requests user promotion to become a validated user.
     *
     * @param id                The ID of the identified user requesting promotion.
     * @param validationRequest The promotion request information.
     * @return The submitted validation request.
     */
    @Transactional
    public ValidationResponse validate(String id, ValidationRequest validationRequest) {

        ValidationStatus status = ValidationStatus.REVIEW;

        var validation = new Validation();

        validationService.hasPromotionRequestWithRegistryActor(id, validationRequest.organisationId, validationRequest.organisationSource, validationRequest.registryActorId);

        if (autoApprove) {
            status = ValidationStatus.APPROVED;
            roleService.assignRolesToUser(id, List.of("validated"));
            validation.setValidatedBy(id);
            validation.setValidatedOn(Timestamp.from(Instant.now()));
        }

        var user = userRepository.findById(id);

        validation.setUser(user);
        validation.setCreatedOn(Timestamp.from(Instant.now()));
        validation.setStatus(status);
        validation.setOrganisationId(validationRequest.organisationId);
        validation.setOrganisationName(validationRequest.organisationName);
        validation.setOrganisationSource(Source.valueOf(validationRequest.organisationSource));
        validation.setOrganisationWebsite(validationRequest.organisationWebsite);
        validation.setOrganisationRole(validationRequest.organisationRole);
        validation.setRegistryActor(registryActorRepository.findById(validationRequest.registryActorId));
        validationService.store(validation);


        CompletableFuture.supplyAsync(() ->
                mailerService.retrieveAdminEmails()
        ).thenAccept(addrs -> {
            mailerService.sendMails(validation, MailType.ADMIN_ALERT_NEW_VALIDATION, addrs);
            if (!addrs.contains(validation.getUser().getEmail())) {
                mailerService.sendMails(validation, MailType.VALIDATED_ALERT_CREATE_VALIDATION, Arrays.asList(validation.getUser().getEmail()));
            }
        });

        return ValidationMapper.INSTANCE.validationToDto(validation);
    }

    /**
     * Delete identified users from the database.
     */
    @Transactional
    public void deleteAll() {

        userRepository.deleteAll();
    }

    /**
     * Adds the deny_access role to the specified user in Keycloak, denying access to the API.
     *
     * @param userId  The unique identifier of the user to whom the access will be restricted.
     * @param adminId The unique identifier of the admin denying access to the user.
     * @param reason  The reason for denying access to the user.
     */
    @Transactional
    public void addDenyAccessRole(String adminId, String userId, String reason) {

        var history = new History();
        history.setAction(reason);
        history.setUserId(adminId);
        history.setPerformedOn(Timestamp.from(Instant.now()));

        var userToBeBanned = userRepository.findById(userId);
        userToBeBanned.setBanned(Boolean.TRUE);
        historyRepository.persist(history);
        roleRepository.assignRoles(userId, List.of("deny_access"));
    }

    /**
     * Removes the deny_access role from the specified user in Keycloak, allowing access to the API.
     *
     * @param userId  The unique identifier of the user to whom the access will be allowed.
     * @param adminId The unique identifier of the admin allowing access to the user.
     * @param reason  The reason for allowing access to the user.
     */
    @Transactional
    public void removeDenyAccessRole(String adminId, String userId, String reason) {

        var history = new History();
        history.setAction(reason);
        history.setUserId(adminId);
        history.setPerformedOn(Timestamp.from(Instant.now()));

        var userToBeBanned = userRepository.findById(userId);
        userToBeBanned.setBanned(Boolean.FALSE);
        historyRepository.persist(history);
        roleRepository.removeRoles(userId, List.of("deny_access"));
    }

    public PageResource<UserRegistryAssessmentEligibilityResponse> getUserRegistryAssessmentEligibility( int page, int size, String userID, UriInfo uriInfo) {

        var list = validationService.getUserRegistryAssessmentEligibility(page, size, userID);

        return new PageResource<>(list, UserMapper.INSTANCE.listOfUserRegistryRegistryAssessmentEligibilityToDto(list.list()), uriInfo);
    }
    public PageResource<UserRegistryAssessmentEligibilityResponse> getUserRegistryAssessmentEligibilityAll( int page, int size, String userID, UriInfo uriInfo) {

        var list = validationService.getUserRegistryAssessmentEligibilityAll(page, size, userID);

        return new PageResource<>(list, UserMapper.INSTANCE.listOfUserRegistryRegistryAssessmentEligibilityToDto(list.list()), uriInfo);
    }
}