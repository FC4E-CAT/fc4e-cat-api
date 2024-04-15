package org.grnet.cat.repositories;

import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.grnet.cat.entities.*;
import org.grnet.cat.enums.UserType;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * The IdentifiedRepository interface provides data access methods for the User entity.
 */
@ApplicationScoped
public class UserRepository implements UserRepositoryI<User, String> {

    @Inject
    @Named("keycloak-repository")
    RoleRepository roleRepository;

    @ConfigProperty(name = "api.keycloak.user.id")
    String attribute;


    /**
     * It executes a query in database to retrieve user's profile.
     *
     * @param id User's Unique identifier (voperson_id)
     * @return User's Profile.
     */
    @Override
    public User fetchUser(String id) {

        var roles = roleRepository.fetchUserRoles(id);
        var user = find("from User user where user.id = ?1", id).firstResult();
        user.setRoles(roles);
        user.setType(findUserType(roles));

        return user;
    }

    /**
     * Retrieves a page of users from the database.
     *
     * @param search  Enables clients to specify a text string for searching specific fields within User entity.
     * @param sort Specifies the field by which the results to be sorted.
     * @param order Specifies the order in which the sorted results should be returned.
     * @param status Indicates whether the user is active or deleted.
     * @param type Filters the results based on the type of user.
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of users to include in a page.
     * @return A list of UserProfile objects representing the users in the
     * requested page.
     */
    public PageQuery<User> fetchUsersByPage(String search, String sort, String order, String status, String type, int page, int size) {

        var joiner = new StringJoiner(StringUtils.SPACE);
        joiner.add("from User user");

        var map = new HashMap<String, Object>();

        if(StringUtils.isNotEmpty(status)){

            joiner.add("where user.banned = :banned");
            map.put("banned", status.equals("active") ? Boolean.FALSE : Boolean.TRUE);
        } else {

            var list = new ArrayList<>();

            list.add(Boolean.TRUE);
            list.add(Boolean.FALSE);

            joiner.add("where user.banned in (:banned)");
            map.put("banned", list);
        }

        if(StringUtils.isNotEmpty(type)){

            var usersForSearching = roleRepository.fetchRolesMembers(UserType.getRoleByType(UserType.valueOf(type)));

            var vopersonIds = usersForSearching
                    .stream()
                    .filter(user->{
                        var roles = roleRepository.fetchUserRoles(user.getAttributes().get(attribute).get(0));
                        var userType = findUserType(roles);

                        return UserType.valueOf(type).equals(userType);})
                    .map(users -> users.getAttributes().get(attribute))
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());

            joiner.add("and user.id in (:vopersonIds)");
            map.put("vopersonIds", vopersonIds);
        }

        if (StringUtils.isNotEmpty(search)) {

            joiner.add("and user.id like :search or user.surname like :search or user.name like :search or user.email like :search or user.orcidId like :search");
            map.put("search", "%" + search + "%");
        }

        joiner.add("order by");
        joiner.add("user."+sort);
        joiner.add(order);

        var panache = find(joiner.toString(), map).page(page, size);

        var users =  panache.list();

        var addedRolesAndUserType = users
                .stream()
                .map(user->{
                    var roles = roleRepository.fetchUserRoles(user.getId());
                    user.setRoles(roles);
                    user.setType(findUserType(roles));

                    return user;
                }).collect(Collectors.toList());

        var pageable = new PageQueryImpl<User>();

        pageable.list = addedRolesAndUserType;
        pageable.index = page;
        pageable.size = size;
        pageable.count = panache.count();
        pageable.page = Page.of(page, size);

        return pageable;
    }

    /**
     * Updates the metadata for a user's profile.
     *
     * @param id The ID of the user whose metadata is being updated.
     * @param name The user's name.
     * @param surname The user's surname.
     * @param email The user's email address.
     * @param orcidId The user's orcid id.
     * @return The updated user's profile
     */
    @Override
    @Transactional
    public User updateUserMetadata(String id, String name, String surname, String email, String orcidId) {

        var user = findById(id);

        user.setName(name);
        user.setSurname(surname);
        user.setEmail(email);
        if(StringUtils.isNotEmpty(orcidId)){
            user.setOrcidId(orcidId);
        }
        user.setUpdatedOn(Timestamp.from(Instant.now()));

        return user;
    }

    public UserType findUserType(List<Role> roles){

        var set = roles
                .stream()
                .map(Role::getName)
                .map(UserType::retrieveByRole)
                .collect(Collectors.toSet());

        return UserType.mostSeverity(set);
    }
}
