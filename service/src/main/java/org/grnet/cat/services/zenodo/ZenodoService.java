package org.grnet.cat.services.zenodo;

import io.quarkus.hibernate.validator.runtime.interceptor.MethodValidated;
import io.quarkus.logging.Log;
import io.quarkus.security.ForbiddenException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.WebApplicationException;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.grnet.cat.constraints.ValidZenodoAction;
import org.grnet.cat.dtos.assessment.ZenodoAssessmentInfoResponse;
import org.grnet.cat.dtos.assessment.registry.UserJsonRegistryAssessmentResponse;
import org.grnet.cat.entities.MotivationAssessment;
import org.grnet.cat.entities.User;
import org.grnet.cat.entities.ZenodoAssessmentInfo;
import org.grnet.cat.entities.ZenodoAssessmentInfoId;
import org.grnet.cat.enums.MailType;
import org.grnet.cat.enums.ShareableEntityType;
import org.grnet.cat.enums.ZenodoState;
import org.grnet.cat.mappers.AssessmentMapper;
import org.grnet.cat.mappers.ZenodoAssessmentInfoMapper;
import org.grnet.cat.repositories.MotivationAssessmentRepository;
import org.grnet.cat.repositories.UserRepository;
import org.grnet.cat.repositories.ZenodoAssessmentInfoRepository;
import org.grnet.cat.services.KeycloakAdminService;
import org.grnet.cat.services.MailerService;
import org.grnet.cat.services.interceptors.ShareableEntity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.time.Instant;
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
    @RestClient
    ZenodoClient zenodoClient;


    @ConfigProperty(name = "zenodo.api.key")
    String API_KEY;


    @Inject
    MotivationAssessmentRepository motivationAssessmentRepository;

    @Inject
    UserRepository userRepository;
    @Inject
    KeycloakAdminService keycloakAdminService;

    @Inject
    ZenodoAssessmentInfoRepository zenodoAssessmentInfoRepository;
    private final ExecutorService executorService = Executors.newFixedThreadPool(2); // Adjust as needed


    public String getAccessToken() {
        return "Bearer " + API_KEY;
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

        CompletableFuture.runAsync(() -> {
            try {
                // Call the method to run the steps asynchronously
                runStepsInSequence(assessment, binaryContent, activeUser, sharedUserIds)
                        .join(); // Ensures the process completes in the background
            } catch (Exception e) {
                // Log the error but do not affect the user response
                System.err.println("Error in async publishing process: " + e.getMessage());
            }
        });
        return "Process of uploading assessment with ID: " + assessmentId + "  has started, it may take some time.. " +
                "You will be informed via email when assessment is uploaded to zenodo. ";
    }

    @ShareableEntity(type = ShareableEntityType.ASSESSMENT, id = String.class)
    @Transactional
    public ZenodoAssessmentInfoResponse getAssessment(String assessmentId) throws IOException, InterruptedException {


        var assessmentOpt = zenodoAssessmentInfoRepository.getAssessmentByAsessmentId(assessmentId);
        if (assessmentOpt.isEmpty()) {
            throw new RuntimeException("Assessment not found in zenodo for ID: " + assessmentId);
        }

        return ZenodoAssessmentInfoMapper.INSTANCE.zenodoAssessmentInfoToResponse(assessmentOpt.get());
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

        var zenodoResponse = zenodoClient.getDeposit(getAccessToken(), depositId);
        if (zenodoResponse.containsKey("submitted")) {
            if (Boolean.TRUE.equals(zenodoResponse.get("submitted"))) {
                stateRef.set(ZenodoState.DEPOSIT_PUBLISHED);
            }
        }

        CompletableFuture.runAsync(() -> {
                    if (!stateRef.get().equals(ZenodoState.DEPOSIT_PUBLISHED)) {
                        zenodoClient.publishDeposit(getAccessToken(), depositId);

                    }
                })
                .thenApplyAsync(v -> {
                    stateRef.set(ZenodoState.DEPOSIT_PUBLISHED);
                    //try {
                    var zenodoAssessment = updateInDatabase(zenodoAssessmentInfoRef.get());
                    if (zenodoAssessment != null) {
                        zenodoAssessmentInfoRef.set(zenodoAssessment);
                    }
                    return zenodoAssessmentInfoRef.get();
                })
                .thenRunAsync(() -> {
                            stateRef.set(ZenodoState.PROCESS_COMPLETED);
                            mailerService.sendMails(
                                    zenodoAssessmentInfoRef.get().getAssessment(),
                                    depositId,
                                    activeUser.getName(),
                                    MailType.ZENODO_PUBLISH_DEPOSIT,
                                    List.of(activeUser.getEmail())
                            );
                        }
                )
                .thenApply(v -> ZenodoAssessmentInfoMapper.INSTANCE.zenodoAssessmentInfoToResponse(zenodoAssessmentInfoRef.get()))
                .exceptionally(ex -> {
                    Log.error("Error publishing deposit to Zenodo", ex);
                    if (stateRef.get().equals(ZenodoState.DEPOSIT_PUBLISHED)) {
                        mailerService.sendMails(
                                zenodoAssessmentInfoRef.get().getAssessment(),
                                depositId,
                                activeUser.getName(),
                                MailType.ZENODO_PUBLISH_DEPOSIT_DRAFT_IN_DB,
                                List.of(activeUser.getEmail())
                        );
                    } else if (stateRef.get().equals(ZenodoState.FILE_UPLOADED_TO_DEPOSIT)) {
                        mailerService.sendMails(
                                zenodoAssessmentInfoRef.get().getAssessment(),
                                depositId,
                                activeUser.getName(),
                                MailType.ZENODO_FAILED_PUBLISH_DEPOSIT,
                                List.of(activeUser.getEmail())
                        );

                    }
                    return ZenodoAssessmentInfoMapper.INSTANCE.zenodoAssessmentInfoToResponse(zenodoAssessmentInfoRef.get());  // Avoid rethrowing if you want graceful failure handling
                });

    }


    private void uploadFile(MotivationAssessment assessment, byte[] binaryContent, String depositId) throws IOException {
        File tempFile = new File(assessment.getId());
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(binaryContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Step 3: Upload file to Zenodo
        upload(tempFile, String.valueOf(depositId));

    }

    public CompletableFuture<Void> runStepsInSequence(MotivationAssessment assessment, byte[] binaryContent, User activeUser, List<String> sharedUsersIds) {
        final AtomicReference<ZenodoState> state = new AtomicReference<>(ZenodoState.PROCESS_INIT);
        final AtomicReference<String> depositIdRef = new AtomicReference<>(null);
        final AtomicReference<ZenodoAssessmentInfo> zenodoAssessmentInfoRef = new AtomicReference<>(null);

        return CompletableFuture
                .supplyAsync(() -> {
                    // Step 1: Preparation of assessment for Zenodo
                    System.out.println("Step 1: Preparing assessment for Zenodo...");
                    var metadata = createMetadata(assessment, activeUser, sharedUsersIds);
                    var response = createDeposit(metadata);
                    var depositId = response.get("id");
                    if (depositId == null) {
                        throw new RuntimeException("Failed to create deposit in Zenodo for assessment ID: " + assessment.getId());
                    }
                    depositIdRef.set(String.valueOf(depositId));
                    return depositId;
                }, executorService)
                .thenApply(depositId -> {
                    // Step 2: Upload to Zenodo
                    state.set(ZenodoState.DEPOSIT_CREATED);

                    System.out.println("Step 2: Uploading to Zenodo...");
                    try {
                        uploadFile(assessment, binaryContent, depositIdRef.get());
                        return depositId;
                    } catch (IOException e) {

                        zenodoClient.deleteDeposit(getAccessToken(), depositIdRef.get()); // Cleanup
                        state.set(ZenodoState.PROCESS_FAILED);

                        throw new RuntimeException("Error uploading file to Zenodo: " + e.getMessage());
                    }
                })
                .thenApply(depositId -> {
                    // Step 3: Write to database BEFORE publishing
                    state.set(ZenodoState.FILE_UPLOADED_TO_DEPOSIT);

                    System.out.println("Step 3: Writing to DB before publishing...");
                    try {
                        var zenodoAssessmentInfo = createInDatabase(assessment, depositIdRef.get(), state.get());
                        zenodoAssessmentInfoRef.set(zenodoAssessmentInfo);
                        return depositId;
                    } catch (Exception dbException) {
                        System.err.println("DB write failed, rolling back Zenodo deposit...");
                        zenodoClient.deleteDeposit(getAccessToken(), depositIdRef.get()); // Cleanup
                        state.set(ZenodoState.PROCESS_FAILED);
                        mailerService.sendMails(assessment, depositIdRef.get(), activeUser.getName(), MailType.ZENODO_FAILED_PUBLISH_PROCESS, List.of(activeUser.getEmail()));

                        throw new RuntimeException("DB write failed, rolling back deposit: " + dbException.getMessage());
                    }
                })
                .thenApply(depositId -> {
                    // Step 4: Publish deposit only after DB write succeeds
                    System.out.println("Step 4: Publishing deposit...");
                    publishDeposit(depositIdRef.get());

                    return depositId;
                })
                .thenAccept(depositId -> {
                    // Final step: Send notification if everything succeeded
                    state.set(ZenodoState.DEPOSIT_PUBLISHED);

                    var zenodoAssessmentInfo = updateInDatabase(zenodoAssessmentInfoRef.get());

                    zenodoAssessmentInfoRef.set(zenodoAssessmentInfo);
                    state.set(ZenodoState.PROCESS_COMPLETED);
                    if (state.get().equals(ZenodoState.DEPOSIT_PUBLISHED)) {
                        mailerService.sendMails(assessment, depositIdRef.get(), activeUser.getName(), MailType.ZENODO_PUBLISH_ASSESSMENT, List.of(activeUser.getEmail()));
                        System.out.println("Step 5: Email sent successfully...");
                    } else if (state.get().equals(ZenodoState.PROCESS_COMPLETED)) {
                        mailerService.sendMails(assessment, depositIdRef.get(), activeUser.getName(), MailType.ZENODO_COMPLETED_PUBLISH_PROCESS, List.of(activeUser.getEmail()));
                        System.out.println("Step 5: Email sent successfully...");
                    }
                })
                .exceptionally(ex -> {
                    // Handle any failures in previous steps
                    if (state.get() != ZenodoState.DEPOSIT_PUBLISHED && state.get() != ZenodoState.FILE_UPLOADED_TO_DEPOSIT) { // Only delete if not yet published, when process fails during create deposit, upload file
                        state.set(ZenodoState.PROCESS_FAILED);
                        if (depositIdRef.get() != null) {
                            zenodoClient.deleteDeposit(getAccessToken(), depositIdRef.get());
                        }
                        mailerService.sendMails(assessment, null, activeUser.getName(), MailType.ZENODO_FAILED_PUBLISH_PROCESS, List.of(activeUser.getEmail()));
                    } else if (state.get() == ZenodoState.FILE_UPLOADED_TO_DEPOSIT) { //the process has failed when try to publish the deposit, the deposit remains in draft state in zenodo
                        mailerService.sendMails(assessment, depositIdRef.get(), activeUser.getName(), MailType.ZENODO_DRAFT_DEPOSIT, List.of(activeUser.getEmail()));
                    } else { //the process fails when trying to update the database , the deposit is published but this info does not appear in db
                        mailerService.sendMails(assessment, depositIdRef.get(), activeUser.getName(), MailType.ZENODO_PUBLISH_ASSESSMENT, List.of(activeUser.getEmail()));

                    }
                    return null;
                });

    }


    @Transactional
    public ZenodoAssessmentInfo updateInDatabase(ZenodoAssessmentInfo zenodoAssessmentInfo) {
        var managedAssessment = zenodoAssessmentInfoRepository.getAssessmentByDepositIdAndAssessmentId(zenodoAssessmentInfo.getId().getDepositId(), zenodoAssessmentInfo.getId().getAssessmentId());
        if (managedAssessment.isEmpty()) {
            return null;
        }
        zenodoAssessmentInfo = managedAssessment.get();

        zenodoAssessmentInfo.setPublishedAt(Timestamp.from(Instant.now()));
        zenodoAssessmentInfo.setIsPublished(Boolean.TRUE);
        zenodoAssessmentInfo.setZenodoState(ZenodoState.PROCESS_COMPLETED);
        zenodoAssessmentInfoRepository.persist(zenodoAssessmentInfo);
        return zenodoAssessmentInfo;
    }


    @Transactional
    public ZenodoAssessmentInfo createInDatabase(MotivationAssessment assessment, String depositId, ZenodoState state) {

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
        zenodoAssessmentInfoRepository.persist(zenodoAssessmentInfo);
        return zenodoAssessmentInfo;
    }


    public void upload(File fileContent, String depositionId) throws IOException {

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
        Map<String, Object> response = zenodoClient.uploadFile(getAccessToken(), depositionId, requestBody, "multipart/form-data; boundary=" + boundary);
        // Check the response and handle accordingly
        if (response.containsKey("id")) {
            Log.info("File uploaded successfully: " + response);
        } else {
            Log.info("Upload failed with response: " + response);
        }

        // Delete the file after upload (optional)
        fileContent.delete();
    }

    public Map<String, Object> createDeposit(Map<String, Object> metadata) {
        Map<String, Object> deposit = zenodoClient.createDeposit(getAccessToken(), metadata);

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
                "creators", creators));
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
}
