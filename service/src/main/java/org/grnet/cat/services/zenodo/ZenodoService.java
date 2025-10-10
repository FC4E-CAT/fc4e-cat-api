package org.grnet.cat.services.zenodo;

import io.quarkus.hibernate.validator.runtime.interceptor.MethodValidated;
import io.quarkus.logging.Log;
import io.quarkus.security.ForbiddenException;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.WebApplicationException;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.grnet.cat.constraints.ValidZenodoAction;
import org.grnet.cat.dtos.assessment.ZenodoAssessmentInfoResponse;
import org.grnet.cat.dtos.assessment.zenodo.ZenodoDepositResponse;
import org.grnet.cat.dtos.assessment.registry.UserJsonRegistryAssessmentResponse;
import org.grnet.cat.entities.*;
import org.grnet.cat.enums.MailType;
import org.grnet.cat.enums.ShareableEntityType;
import org.grnet.cat.enums.ZenodoState;
import org.grnet.cat.mappers.AssessmentMapper;
import org.grnet.cat.mappers.ZenodoAssessmentInfoMapper;
import org.grnet.cat.repositories.MotivationAssessmentRepository;
import org.grnet.cat.repositories.SettingRepository;
import org.grnet.cat.repositories.UserRepository;
import org.grnet.cat.repositories.ZenodoAssessmentInfoRepository;
import org.grnet.cat.services.KeycloakAdminService;
import org.grnet.cat.services.MailerService;
import org.grnet.cat.services.SettingService;
import org.grnet.cat.services.env.EnvironmentDetector;
import org.grnet.cat.services.interceptors.ShareableEntity;
import org.grnet.cat.utils.Utility;
import org.grnet.cat.utils.ZenodoConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import static org.grnet.cat.services.KeycloakAdminService.ENTITLEMENTS_DELIMITER;

@ApplicationScoped
@MethodValidated
public class ZenodoService {

    @Inject
    MotivationAssessmentRepository motivationAssessmentRepository;

    @Inject
    UserRepository userRepository;
    @Inject
    KeycloakAdminService keycloakAdminService;

    @Inject
    ZenodoAssessmentInfoRepository zenodoAssessmentInfoRepository;

    @Inject
    SettingService settingService;

    @Inject
    SettingRepository settingRepository;

    @Inject
    EnvironmentDetector environmentDetector;

    @Inject
    ZenodoClientFactory zenodoClientFactory;


    @Inject
    ZenodoConfig zenodoConfig;

    private final ExecutorService executorService = Executors.newFixedThreadPool(2); // Adjust as needed

    private ZenodoClient zenodoClient;

    private String zenodoBaseUrl;

    @PostConstruct
    void initClient() {
        this.zenodoBaseUrl = environmentDetector.getZenodoBaseUrl();
        System.out.println("[ZenodoService] Initializing Zenodo client with base URL: " + zenodoBaseUrl);
        this.zenodoClient = zenodoClientFactory.create(zenodoBaseUrl);
    }

    public String getAccessToken() {
        var token = settingService.getSettingConfig("1", "zenodo.api.key");
        return "Bearer " + token.orElseThrow(() -> new IllegalStateException("Zenodo API key is not configured."));
    }

    @Inject
    MailerService mailerService;

    @ShareableEntity(type = ShareableEntityType.ASSESSMENT, id = String.class)
    @Transactional
    public String publishAssessment(
            @ValidZenodoAction(repository = MotivationAssessmentRepository.class, message = "Action not permitted for assessment: ")
            String assessmentId, byte[] binaryContent, String userId) {
        var assessment = motivationAssessmentRepository.findById(assessmentId);
        if (assessment == null) {
            throw new NotFoundException("Assessment not found for ID: " + assessmentId);
        }

        var zenodoAssessmentInfoOpt = zenodoAssessmentInfoRepository.getAssessmentByAsessmentId(assessmentId);
        if (zenodoAssessmentInfoOpt.isPresent()) {
            String isPublished = zenodoAssessmentInfoOpt.get().getIsPublished() ? "PUBLISHED" : "DRAFT";
            throw new RuntimeException("Assessment with ID: " + assessmentId + " is already in Zenodo under deposit with ID: " + zenodoAssessmentInfoOpt.get().getId().getDepositId() + " and it's publication status is: " + isPublished);

        }
        var activeUser = userRepository.fetchUser(userId);
        List<String> sharedUserIds = keycloakAdminService.getIdsOfSharedUsers(
                ShareableEntityType.ASSESSMENT.getValue().concat(ENTITLEMENTS_DELIMITER).concat(assessment.getId())
        );

        // Check parent to decide if a new version should be created
        Optional<ZenodoAssessmentInfo> parentZenodoOpt = Optional.empty();
        var parentId = assessment.getParentAssessmentId();

        if (!assessment.getId().equals(parentId)) {
            parentZenodoOpt = zenodoAssessmentInfoRepository.getAssessmentByAsessmentId(parentId);
            if (parentZenodoOpt.isPresent() && !parentZenodoOpt.get().getIsPublished()) {
                // if parent is unpublished, ignore it
                parentZenodoOpt = Optional.empty();
            }
        }

        Optional<ZenodoAssessmentInfo> finalParentZenodoOpt = parentZenodoOpt;
        CompletableFuture.runAsync(() -> {
            try {
                runStepsInSequence(assessment, binaryContent, activeUser, sharedUserIds, finalParentZenodoOpt)
                        .join(); // Ensures the process completes in the background
            } catch (Exception e) {
                // Log the error but do not affect the user response
                System.err.println("Error in async publishing process: " + e.getMessage());
            }
        });
        return "Process of uploading assessment with ID: " + assessmentId + "  has started, it may take some time.. " +
                "You will be informed via email when assessment is uploaded to zenodo. ";
    }

    // @ShareableEntity(type = ShareableEntityType.ASSESSMENT, id = String.class)
    @Transactional
    public ZenodoAssessmentInfoResponse getAssessment(String assessmentId, Utility utility) {

        var assessmentOpt = zenodoAssessmentInfoRepository.getAssessmentByAsessmentId(assessmentId);
        if (assessmentOpt.isEmpty()) {
            throw new RuntimeException("Not found zenodo assessment information for assessment with ID: " + assessmentId + " to exist in CAT");
        }

        if (assessmentOpt.isPresent() && !assessmentOpt.get().getIsPublished()) {
            try {
                var userIdentifier = utility.getUserUniqueIdentifier();

            } catch (BadRequestException e) {
                throw new BadRequestException("Please ensure you are logged in to CAT, in order to view assessment in draft status.");
            }
            var dbAssessmentToJson = AssessmentMapper.INSTANCE.zenodoUserRegistryAssessmentToJsonAssessment(
                    assessmentOpt.get().getAssessment(), utility.getUserUniqueIdentifier()
            );

            List<String> sharedUserIds = keycloakAdminService.getIdsOfSharedUsers(
                    ShareableEntityType.ASSESSMENT.getValue() + ENTITLEMENTS_DELIMITER + assessmentOpt.get().getAssessment().getId()
            );

            // Ensure the user has access
            if (!dbAssessmentToJson.getUserId().equals(utility.getUserUniqueIdentifier()) && !sharedUserIds.contains(utility.getUserUniqueIdentifier())) {
                throw new BadRequestException("User does not have permission to view this zenodo assessment info.");
            }

        }

        return ZenodoAssessmentInfoMapper.INSTANCE.zenodoAssessmentInfoToResponse(assessmentOpt.get(), zenodoBaseUrl);
    }

    @Transactional
    public ZenodoAssessmentInfoResponse getAdminAssessment(String assessmentId) {

        var assessmentOpt = zenodoAssessmentInfoRepository.getAssessmentByAsessmentId(assessmentId);
        if (assessmentOpt.isEmpty()) {
            throw new RuntimeException("Not found zenodo assessment information for assessment with ID: " + assessmentId + " to exist in CAT");
        }
        return ZenodoAssessmentInfoMapper.INSTANCE.zenodoAssessmentInfoToResponse(assessmentOpt.get(), zenodoBaseUrl);
    }

    @Transactional
    public ZenodoDepositResponse getDeposit(String depositId, Utility utility) {

        var zenodoAssessmentInfoOpt = zenodoAssessmentInfoRepository.getAssessmentByDepositId(depositId);
        if (zenodoAssessmentInfoOpt.isEmpty()) {
            throw new RuntimeException("Not found zenodo assessment information for deposit with ID: " + depositId);
        }
        var zenodoAssessmentInfo = zenodoAssessmentInfoOpt.get();
        if (!zenodoAssessmentInfo.getIsPublished()) {
            try {
                var userIdentifier = utility.getUserUniqueIdentifier();

            } catch (BadRequestException e) {

                throw new BadRequestException("Please ensure you are logged in to CAT in order to view deposit in draft status.");
            }

            var dbAssessmentToJson = AssessmentMapper.INSTANCE.zenodoUserRegistryAssessmentToJsonAssessment(zenodoAssessmentInfo.getAssessment(), utility.getUserUniqueIdentifier());


            List<String> sharedUserIds = keycloakAdminService.getIdsOfSharedUsers(
                    ShareableEntityType.ASSESSMENT.getValue() + ENTITLEMENTS_DELIMITER + zenodoAssessmentInfo.getAssessment().getId()
            );

            // Ensure the user has access
            if (!dbAssessmentToJson.getUserId().equals(utility.getUserUniqueIdentifier()) && !sharedUserIds.contains(utility.getUserUniqueIdentifier())) {
                throw new BadRequestException("User does not have permission to get info of this deposit.");
            }

        }
        var response = zenodoClient.getDeposit(getAccessToken(), depositId);
        return ZenodoAssessmentInfoMapper.INSTANCE.entityToZenodoDepositResponse(response);
    }

    @Transactional
    public ZenodoDepositResponse getAdminDeposit(String depositId) {

        var zenodoAssessmentInfoOpt = zenodoAssessmentInfoRepository.getAssessmentByDepositId(depositId);
        if (zenodoAssessmentInfoOpt.isEmpty()) {
            throw new RuntimeException("Not found zenodo assessment information for deposit with ID: " + depositId);
        }

        var response = zenodoClient.getDeposit(getAccessToken(), depositId);
        return ZenodoAssessmentInfoMapper.INSTANCE.entityToZenodoDepositResponse(response);
    }

    @Transactional
    public void publishDepositToZenodo(String depositId, String userId) {


        var zenodoAssessmentInfoOpt = zenodoAssessmentInfoRepository.getAssessmentByDepositId(depositId);
        if (zenodoAssessmentInfoOpt.isEmpty()) {
            throw new NotFoundException("No assessment info found for deposit with ID: " + depositId + " in service");
        }
        var zenodoAssessmentInfo = zenodoAssessmentInfoOpt.get();
        var dbAssessmentToJson = AssessmentMapper.INSTANCE.zenodoUserRegistryAssessmentToJsonAssessment(zenodoAssessmentInfo.getAssessment(), userId);

        List<String> sharedUserIds = keycloakAdminService.getIdsOfSharedUsers(
                ShareableEntityType.ASSESSMENT.getValue() + ENTITLEMENTS_DELIMITER + zenodoAssessmentInfo.getAssessment().getId()
        );
        // Ensure the user has access
        if (!dbAssessmentToJson.getUserId().equals(userId) && !sharedUserIds.contains(userId)) {
            throw new ForbiddenException("User does not have permission to publish this assessment.");
        }

        if (zenodoAssessmentInfo.getIsPublished()) {
            throw new RuntimeException("The deposit with ID: " + depositId + " is already published to Zenodo");
        }
        var activeUser = userRepository.fetchUser(userId);
        final AtomicReference<ZenodoAssessmentInfo> zenodoAssessmentInfoRef = new AtomicReference<>(zenodoAssessmentInfo);
        final AtomicReference<ZenodoState> stateRef = new AtomicReference<>(ZenodoState.FILE_UPLOADED_TO_DEPOSIT);
        final AtomicReference<String> doiRef = new AtomicReference<>(null);
        final AtomicReference<String> imageUrlRef = new AtomicReference<>(null);
        final AtomicReference<String> targetUrlRef = new AtomicReference<>(null);

        var zenodoResponse = zenodoClient.getDeposit(getAccessToken(), depositId);
        if (zenodoResponse.containsKey("submitted")) {
            if (Boolean.TRUE.equals(zenodoResponse.get("submitted"))) {
                stateRef.set(ZenodoState.DEPOSIT_PUBLISHED);
            }
        }
        String accessToken = getAccessToken();
        CompletableFuture.runAsync(() -> {
                    if (!stateRef.get().equals(ZenodoState.DEPOSIT_PUBLISHED)) {
                        var response = zenodoClient.publishDeposit(accessToken, depositId);

                        doiRef.set(extractDoiFromResponse(response));
                        imageUrlRef.set(extractImageUrlFromResponse(response, doiRef.get()));
                        targetUrlRef.set(extractTargetUrlFromResponse(doiRef.get()));

                    }
                })
                .thenApplyAsync(v -> {
                    stateRef.set(ZenodoState.DEPOSIT_PUBLISHED);
                    //try {
                    var zenodoAssessment = updateInDatabase(zenodoAssessmentInfoRef.get(), doiRef.get(), imageUrlRef.get(), targetUrlRef.get());
                    if (zenodoAssessment != null) {
                        zenodoAssessmentInfoRef.set(zenodoAssessment);
                    }
                    return zenodoAssessmentInfoRef.get();
                })
                .thenRunAsync(() -> {
                            stateRef.set(ZenodoState.PROCESS_COMPLETED);
                            if (activeUser.getEmail() != null && !activeUser.getEmail().isEmpty()) {
                                mailerService.sendMails(
                                        zenodoAssessmentInfoRef.get().getAssessment(),
                                        depositId,
                                        activeUser.getName(),
                                        MailType.ZENODO_PUBLISH_DEPOSIT,
                                        List.of(activeUser.getEmail())
                                );
                            }
                        }
                )
                .thenApply(v -> ZenodoAssessmentInfoMapper.INSTANCE.zenodoAssessmentInfoToResponse(zenodoAssessmentInfoRef.get(), zenodoBaseUrl))
                .exceptionally(ex -> {
                    Log.error("Error publishing deposit to Zenodo", ex);
                    if (stateRef.get().equals(ZenodoState.DEPOSIT_PUBLISHED)) {
                        if (activeUser.getEmail() != null && !activeUser.getEmail().isEmpty()) {

                            mailerService.sendMails(
                                    zenodoAssessmentInfoRef.get().getAssessment(),
                                    depositId,
                                    activeUser.getName(),
                                    MailType.ZENODO_PUBLISH_DEPOSIT_DRAFT_IN_DB,
                                    List.of(activeUser.getEmail())
                            );
                        }
                    } else if (stateRef.get().equals(ZenodoState.FILE_UPLOADED_TO_DEPOSIT)) {
                        if (activeUser.getEmail() != null && !activeUser.getEmail().isEmpty()) {

                            mailerService.sendMails(
                                    zenodoAssessmentInfoRef.get().getAssessment(),
                                    depositId,
                                    activeUser.getName(),
                                    MailType.ZENODO_FAILED_PUBLISH_DEPOSIT,
                                    List.of(activeUser.getEmail())
                            );
                        }
                    }
                    return ZenodoAssessmentInfoMapper.INSTANCE.zenodoAssessmentInfoToResponse(zenodoAssessmentInfoRef.get(), zenodoBaseUrl);  // Avoid rethrowing if you want graceful failure handling
                });

    }

    private void uploadFile(String accessToken, MotivationAssessment assessment, byte[] binaryContent, String depositId) throws IOException {
        File tempFile = new File(assessment.getId() + ".pdf");
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(binaryContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Step 3: Upload file to Zenodo
        upload(accessToken, tempFile, String.valueOf(depositId));
    }

    public CompletableFuture<Void> runStepsInSequence(MotivationAssessment assessment, byte[] binaryContent, User activeUser, List<String> sharedUsersIds, Optional<ZenodoAssessmentInfo> parentZenodoOpt) {
        final AtomicReference<ZenodoState> state = new AtomicReference<>(ZenodoState.PROCESS_INIT);
        final AtomicReference<String> depositIdRef = new AtomicReference<>(null);
        final AtomicReference<ZenodoAssessmentInfo> zenodoAssessmentInfoRef = new AtomicReference<>(null);
        final AtomicReference<String> fileUrlRef = new AtomicReference<>(null);
        final AtomicReference<String> doiRef = new AtomicReference<>(null);
        final AtomicReference<String> imageUrlRef = new AtomicReference<>(null);
        final AtomicReference<String> targetUrlRef = new AtomicReference<>(null);
        String accessToken = getAccessToken();
        return CompletableFuture
                .supplyAsync(() -> {
                    // Step 1: Preparation of assessment for Zenodo
                    System.out.println("Step 1: Preparing assessment for Zenodo...");
                    var metadata = createMetadata(assessment, activeUser, sharedUsersIds);
                    Map<String, Object> response;

                    if (parentZenodoOpt.isPresent() && parentZenodoOpt.get().getIsPublished()) {
                        String parentDepositId = parentZenodoOpt.get().getId().getDepositId();
                        System.out.println("Creating new version from parent deposit: " + parentDepositId);
                        response = zenodoClient.createNewVersion(accessToken, parentDepositId);

                        var links = (Map<String, Object>) response.get("links");
                        String latestDraftUrl = (String) links.get("latest_draft");
                        String extractedId = extractIdFromUrl(latestDraftUrl);

                        if (extractedId == null) {
                            throw new RuntimeException("Failed to extract draft deposit ID from Zenodo response.");
                        }

                        zenodoClient.updateDeposit(accessToken, extractedId, metadata); // full payload including "metadata" key

                        depositIdRef.set(extractedId);
                    } else {
                        System.out.println("Creating new deposit from scratch");
                        response = createDeposit(accessToken, metadata);

                        var depositId = response.get("id");
                        if (depositId == null) {
                            throw new RuntimeException("Failed to create deposit in Zenodo for assessment ID: " + assessment.getId());
                        }
                        depositIdRef.set(String.valueOf(depositId));

                    }

                    return depositIdRef.get();
                }, executorService)
                .thenApply(depositId -> {
                    // Step 2: Upload to Zenodo
                    state.set(ZenodoState.DEPOSIT_CREATED);
                    try {
                        uploadFile(accessToken, assessment, binaryContent, depositIdRef.get());
                        return depositId;
                    } catch (IOException e) {

                        zenodoClient.deleteDeposit(accessToken, depositIdRef.get()); // Cleanup
                        state.set(ZenodoState.PROCESS_FAILED);

                        throw new RuntimeException("Error uploading file to Zenodo: " + e.getMessage());
                    }
                })
                .thenApply(depositId -> {
                    // Step 3: Write to database BEFORE publishing
                    state.set(ZenodoState.FILE_UPLOADED_TO_DEPOSIT);

                    System.out.println("Step 3: Writing to DB before publishing...");
                    try {
                        var zenodoAssessmentInfo = createInDatabase(assessment, depositIdRef.get(), state.get(), fileUrlRef.get());
                        zenodoAssessmentInfoRef.set(zenodoAssessmentInfo);
                        return depositId;
                    } catch (Exception dbException) {
                        System.err.println("DB write failed, rolling back Zenodo deposit...");
                        zenodoClient.deleteDeposit(accessToken, depositIdRef.get()); // Cleanup
                        state.set(ZenodoState.PROCESS_FAILED);
                        if (activeUser.getEmail() != null && !activeUser.getEmail().isEmpty()) {

                            mailerService.sendMails(assessment, depositIdRef.get(), activeUser.getName(), MailType.ZENODO_FAILED_PUBLISH_PROCESS, List.of(activeUser.getEmail()));
                        }
                        throw new RuntimeException("DB write failed, rolling back deposit: " + dbException.getMessage());
                    }
                })
                .thenApply(depositId -> {
                    // Step 4: Publish deposit only after DB write succeeds
                    System.out.println("Step 4: Publishing deposit...");
                    System.out.println("Calling publishDeposit for depositId: " + depositIdRef.get());

                    var response = publishDeposit(depositIdRef.get());
                    doiRef.set(extractDoiFromResponse(response));
                    imageUrlRef.set(extractImageUrlFromResponse(response, doiRef.get()));
                    targetUrlRef.set(zenodoConfig.buildDoiUrl(doiRef.get()));

                    return depositId;
                })
                .thenAccept(depositId -> {
                    // Final step: Send notification if everything succeeded
                    state.set(ZenodoState.DEPOSIT_PUBLISHED);

                    var zenodoAssessmentInfo = updateInDatabase(zenodoAssessmentInfoRef.get(), doiRef.get(), imageUrlRef.get(), targetUrlRef.get());
                    zenodoAssessmentInfoRef.set(zenodoAssessmentInfo);
                    state.set(ZenodoState.PROCESS_COMPLETED);
                    if (state.get().equals(ZenodoState.DEPOSIT_PUBLISHED)) {
                        if (activeUser.getEmail() != null && !activeUser.getEmail().isEmpty()) {

                            mailerService.sendMails(assessment, depositIdRef.get(), activeUser.getName(), MailType.ZENODO_PUBLISH_ASSESSMENT, List.of(activeUser.getEmail()));
                        }
                        System.out.println("Step 5: Email sent successfully...");
                    } else if (state.get().equals(ZenodoState.PROCESS_COMPLETED)) {
                        if (activeUser.getEmail() != null && !activeUser.getEmail().isEmpty()) {

                            mailerService.sendMails(assessment, depositIdRef.get(), activeUser.getName(), MailType.ZENODO_COMPLETED_PUBLISH_PROCESS, List.of(activeUser.getEmail()));
                            System.out.println("Step 5: Email sent successfully...");
                        }
                    }
                })
                .exceptionally(ex -> {
                    // Handle any failures in previous steps
                    if (state.get() != ZenodoState.DEPOSIT_PUBLISHED && state.get() != ZenodoState.FILE_UPLOADED_TO_DEPOSIT) { // Only delete if not yet published, when process fails during create deposit, upload file
                        state.set(ZenodoState.PROCESS_FAILED);
                        if (depositIdRef.get() != null) {
                            zenodoClient.deleteDeposit(accessToken, depositIdRef.get());
                        }
                        if (activeUser.getEmail() != null && !activeUser.getEmail().isEmpty()) {

                            mailerService.sendMails(assessment, null, activeUser.getName(), MailType.ZENODO_FAILED_PUBLISH_PROCESS, List.of(activeUser.getEmail()));
                        }
                    } else if (state.get() == ZenodoState.FILE_UPLOADED_TO_DEPOSIT) { //the process has failed when try to publish the deposit, the deposit remains in draft state in zenodo
                        if (activeUser.getEmail() != null && !activeUser.getEmail().isEmpty()) {

                            mailerService.sendMails(assessment, depositIdRef.get(), activeUser.getName(), MailType.ZENODO_DRAFT_DEPOSIT, List.of(activeUser.getEmail()));
                        }
                    } else { //the process fails when trying to update the database , the deposit is published but this info does not appear in db
                        if (activeUser.getEmail() != null && !activeUser.getEmail().isEmpty()) {
                            mailerService.sendMails(assessment, depositIdRef.get(), activeUser.getName(), MailType.ZENODO_PUBLISH_ASSESSMENT, List.of(activeUser.getEmail()));
                        }
                    }
                    return null;
                });

    }


    @Transactional
    public ZenodoAssessmentInfo updateInDatabase(ZenodoAssessmentInfo zenodoAssessmentInfo, String doi, String imageURL, String targetURL) {
        var managedAssessment = zenodoAssessmentInfoRepository.getAssessmentByDepositIdAndAssessmentId(zenodoAssessmentInfo.getId().getDepositId(), zenodoAssessmentInfo.getId().getAssessmentId());
        if (managedAssessment.isEmpty()) {
            return null;
        }
        zenodoAssessmentInfo = managedAssessment.get();
        zenodoAssessmentInfo.setDoi(doi);
        zenodoAssessmentInfo.setImageURL(imageURL);
        zenodoAssessmentInfo.setTargetURL(targetURL);
        zenodoAssessmentInfo.setPublishedAt(Timestamp.from(Instant.now()));
        zenodoAssessmentInfo.setIsPublished(Boolean.TRUE);
        zenodoAssessmentInfo.setZenodoState(ZenodoState.PROCESS_COMPLETED);
        try {
            // ✅ Get the latest Zenodo record info after publication
            var response = zenodoClient.getDeposit(getAccessToken(), zenodoAssessmentInfo.getId().getDepositId());

            Object filesObj = response.get("files");
            if (filesObj instanceof List && !((List<?>) filesObj).isEmpty()) {
                Object first = ((List<?>) filesObj).get(0);
                if (first instanceof Map) {
                    Object flinks = ((Map<?, ?>) first).get("links");
                    if (flinks instanceof Map) {
                        Map<?, ?> links = (Map<?, ?>) flinks;

                        // Sandbox only returns "self", production adds "download"
                        String fileUrl = null;
                        if (links.containsKey("download")) {
                            fileUrl = (String) links.get("download");
                        } else if (links.containsKey("self")) {
                            fileUrl = (String) links.get("self");
                        }

                        if (fileUrl != null) {
                            zenodoAssessmentInfo.setFileUrl(fileUrl);
                            Log.infof("Stored Zenodo file URL: %s", fileUrl);
                        } else {
                            Log.info("Zenodo record has no file links available yet.");
                        }
                    }
                }
            } else {
                Log.info("Zenodo returned no files[] for published record " + zenodoAssessmentInfo.getId().getDepositId());
            }

        } catch (Exception e) {
            Log.warn("Could not fetch file URL for deposit " + zenodoAssessmentInfo.getId().getDepositId(), e);
        }
        zenodoAssessmentInfoRepository.persist(zenodoAssessmentInfo);
        return zenodoAssessmentInfo;
    }


    @Transactional
    public ZenodoAssessmentInfo createInDatabase(MotivationAssessment assessment, String depositId, ZenodoState state, String fileUrl) {

        MotivationAssessment managedAssessment = motivationAssessmentRepository.findById(assessment.getId());
        if (managedAssessment != null) {
            assessment = managedAssessment;
        }
        var zenodoAssessmentInfo = new ZenodoAssessmentInfo();


        // Step 5: Persist to database
        zenodoAssessmentInfo.setAssessment(assessment);
        zenodoAssessmentInfo.setIsPublished(Boolean.FALSE);
        zenodoAssessmentInfo.setUploadedAt(Timestamp.from(Instant.now()));
        zenodoAssessmentInfo.setId(new ZenodoAssessmentInfoId(assessment.getId(), String.valueOf(depositId)));
        zenodoAssessmentInfo.setZenodoState(state);
        zenodoAssessmentInfo.setFileUrl(fileUrl);
        zenodoAssessmentInfoRepository.persist(zenodoAssessmentInfo);
        return zenodoAssessmentInfo;
    }


    public void upload(String accessToken, File fileContent, String depositionId) throws IOException {

        String boundary = UUID.randomUUID().toString();

        // Build the multipart form body
        StringBuilder bodyBuilder = new StringBuilder();
        bodyBuilder.append("--").append(boundary).append("\r\n");
        bodyBuilder.append("Content-Disposition: form-data; name=\"file\"; filename=\"").append(fileContent.getName()).append("\"\r\n");
        bodyBuilder.append("Content-Type: application/pdf\r\n");  // Assuming it's a PDF, adjust if it's another type
        bodyBuilder.append("\r\n");

        // Convert the form data to byte array (the part before the file content)
        byte[] formData = bodyBuilder.toString().getBytes();

        // Prepare the file's binary content
        byte[] fileBytes = Files.readAllBytes(fileContent.toPath());

        // Ending boundary
        String endingBoundary = "\r\n--" + boundary + "--\r\n";
        byte[] endingBoundaryBytes = endingBoundary.getBytes();

        // Combine form data, file data, and the ending boundary
        byte[] requestBody = new byte[formData.length + fileBytes.length + endingBoundaryBytes.length];
        System.arraycopy(formData, 0, requestBody, 0, formData.length);
        System.arraycopy(fileBytes, 0, requestBody, formData.length, fileBytes.length);
        System.arraycopy(endingBoundaryBytes, 0, requestBody, formData.length + fileBytes.length, endingBoundaryBytes.length);

        // Upload the file by calling the Zenodo API method
        Map<String, Object> response = zenodoClient.uploadFile(accessToken, depositionId, requestBody, "multipart/form-data; boundary=" + boundary);
        // Check the response and handle accordingly
        if (response.containsKey("id")) {
            Log.info("File uploaded successfully: " + response);
        } else {
            Log.info("Upload failed with response: " + response);
        }

        // Delete the file after upload (optional)
        fileContent.delete();
    }

    public Map<String, Object> createDeposit(String accessToken, Map<String, Object> metadata) {

        Map<String, Object> deposit = zenodoClient.createDeposit(accessToken, metadata);
        return deposit;
    }

    @Transactional
    public Map<String, Object> publishDeposit(String depositId) {
        try {
            var response = zenodoClient.publishDeposit(getAccessToken(), depositId);
            return response;
        } catch (WebApplicationException e) {
            throw new WebApplicationException(e.getMessage());
        } catch (ProcessingException e) {
            throw new ProcessingException(e.getMessage());
        }
    }

    @Transactional
    public Map<String, Object> createMetadata(MotivationAssessment assessment, User activeUser, List<String> sharedUsersIds) {
        Map<String, Object> metadata = new HashMap<>();

        var dbAssessmentToJson = AssessmentMapper.INSTANCE.zenodoUserRegistryAssessmentToJsonAssessment(assessment, activeUser.getId());
        String title = generateTitle(dbAssessmentToJson);
        String description = "Publishing assessment " + dbAssessmentToJson.assessmentDoc.name + " to Zenodo";
        String uploadType = "dataset";

        List<Map<String, String>> creators = new ArrayList<>();
        List<Map<String, String>> contributors = new ArrayList<>();

        // Add creator to the list
        creators.add(createCreatorOrContributorMap(activeUser));

        if (!sharedUsersIds.isEmpty()) {
            var dbUsers = userRepository.fetchUsers(sharedUsersIds);
            dbUsers.stream()
                    .filter(dbUser -> !dbUser.getId().equals(activeUser.getId())) // Exclude main user
                    .forEach(dbUser -> contributors.add(createCreatorOrContributorMap(dbUser, "editor")));
        }
        // Prepare metadata map
        metadata.put("metadata", Map.of(
                "title", title,
                "upload_type", uploadType,
                "description", description,
                "creators", creators,
                "publication_date", LocalDate.now().toString(),
                "access_right", "open"));
        if (!contributors.isEmpty()) {
            metadata.put("contributors", contributors);
        }
        return metadata;
    }

    private Map<String, String> createCreatorOrContributorMap(User user) {
        return createCreatorOrContributorMap(user, "creator");
    }

    private Map<String, String> createCreatorOrContributorMap(User user, String type) {
        Map<String, String> userMap = new HashMap<>();
        userMap.put("name", user.getName() + " " + user.getSurname());
        userMap.put("type", type);

        if (user.getOrcidId() != null) {
            userMap.put("orcid", user.getOrcidId());
        }

        return userMap;
    }

    private String generateTitle(UserJsonRegistryAssessmentResponse dbAssessmentToJson) {
        return dbAssessmentToJson.assessmentDoc.name + "/"
                + dbAssessmentToJson.assessmentDoc.organisation.name + "/"
                + dbAssessmentToJson.assessmentDoc.actor.getName();
    }

    private String extractIdFromUrl(String url) {
        if (url == null) return null;
        String[] parts = url.split("/");
        return parts.length > 0 ? parts[parts.length - 1] : null;
    }


    @SuppressWarnings("unchecked")
    public boolean isZenodoFeatureEnabled() {
        return settingRepository.findByIdOptional("1")
                .filter(Setting::isEnabled)
                .map(setting -> {
                    var config = (Map<String, Object>) setting.getData().get("config");
                    if (config != null && config.containsKey("zenodo.enabled")) {
                        return Boolean.TRUE.equals(config.get("zenodo.enabled"));
                    }
                    return false;
                })
                .orElse(false);
    }

    private String extractDoiFromResponse(Map<String, Object> response) {
        if (response == null) return null;

        Object doi = response.get("doi");

        if (doi instanceof String && !((String) doi).isEmpty()) {

            return (String) doi;
        }
        return null;
    }

    private String extractImageUrlFromResponse(Map<String, Object> response, String doi) {
        if (response == null) return null;

        Object linksObj = response.get("links");
        if (linksObj instanceof Map) {
            Object badgeObj = ((Map<?, ?>) linksObj).get("badge");
            if (badgeObj instanceof String && !((String) badgeObj).isEmpty()) {
                return  URLDecoder.decode((String) badgeObj, StandardCharsets.UTF_8);

            }
        }

        return null;
    }

    private String extractTargetUrlFromResponse(String doi) {

        if (doi.startsWith("10.5281")) {
            return "https://doi.org/" + doi; // production
        } else if (doi.startsWith("10.5072")) {
            return "https://handle.test.datacite.org/" + doi; // sandbox
        }
        return null;
    }


    @Transactional
    public boolean testZenodoConnection(String token) {
        try {
            ZenodoClient client = zenodoClient;
            client.listDeposits("Bearer " + token);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
