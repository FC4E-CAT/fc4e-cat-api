package org.grnet.cat.services.zenodo;

import io.quarkus.hibernate.validator.runtime.interceptor.MethodValidated;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
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
import org.grnet.cat.enums.ShareableEntityType;
import org.grnet.cat.mappers.AssessmentMapper;
import org.grnet.cat.mappers.ZenodoAssessmentInfoMapper;
import org.grnet.cat.repositories.MotivationAssessmentRepository;
import org.grnet.cat.repositories.UserRepository;
import org.grnet.cat.repositories.ZenodoAssessmentInfoRepository;
import org.grnet.cat.services.KeycloakAdminService;
import org.grnet.cat.services.interceptors.ShareableEntity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

import static org.grnet.cat.services.KeycloakAdminService.ENTITLEMENTS_DELIMITER;

@ApplicationScoped
@MethodValidated
public class ZenodoService {

    @Inject
    @RestClient
    ZenodoClient zenodoClient;
    @ConfigProperty(name = "zenodo.access.token")
    String ACCESS_TOKEN;


    @Inject
    MotivationAssessmentRepository motivationAssessmentRepository;

    @Inject
    UserRepository userRepository;
    @Inject
    KeycloakAdminService keycloakAdminService;

    @Inject
    ZenodoAssessmentInfoRepository zenodoAssessmentInfoRepository;

    @ShareableEntity(type = ShareableEntityType.ASSESSMENT, id = String.class)
    @Transactional
    public ZenodoAssessmentInfoResponse publishAssessment(
            @ValidZenodoAction(repository = MotivationAssessmentRepository.class, message = "Action not permitted for assessment: ")
            String assessmentId, byte[] binaryContent) throws IOException, InterruptedException {

        Object depositId = null; // Store deposit ID to handle rollback
        var assessment = motivationAssessmentRepository.findById(assessmentId);
        if (assessment == null) {
            throw new RuntimeException("Assessment not found for ID: " + assessmentId);
        }
        var zenodoAssessmentOptional = zenodoAssessmentInfoRepository.getLatestByAssessmentId(assessmentId);
        if (zenodoAssessmentOptional.isPresent()) {
            if (zenodoAssessmentOptional.get().getIsPublished()) {
                throw new RuntimeException("Assessment with ID: " + assessmentId + " is already published to Zenodo");
            } else {
                throw new RuntimeException("Assessment with ID: " + assessmentId + " is uploaded to Zenodo but deposit with ID " + zenodoAssessmentOptional.get().getId().getDepositId() + " is not published.");
            }

        }
        var metadata = createMetadata(assessment);
        // Step 1: Create Zenodo deposit
        try {

            var response = createDeposit(ACCESS_TOKEN, metadata);
            depositId = response.get("id");
            if (depositId == null) {
                throw new RuntimeException("Failed to create deposit in Zenodo for assessment ID: " + assessmentId);
            }
            // Step 2: Write binary content to temp file
            File tempFile = new File(assessmentId);
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(binaryContent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // Step 3: Upload file to Zenodo
            upload(tempFile, String.valueOf(depositId));
            var zenodoAssessmentInfo = new ZenodoAssessmentInfo();


            // Step 5: Persist to database
            zenodoAssessmentInfo.setAssessment(assessment);
            zenodoAssessmentInfo.setIsPublished(Boolean.FALSE);
            zenodoAssessmentInfo.setUploadedAt(Timestamp.from(Instant.now()));
            zenodoAssessmentInfo.setId(new ZenodoAssessmentInfoId(assessmentId, String.valueOf(depositId)));
            zenodoAssessmentInfoRepository.persist(zenodoAssessmentInfo);

            // Step 4: Publish deposit
            publishDeposit(ACCESS_TOKEN, String.valueOf(depositId));
            zenodoAssessmentInfo.setPublishedAt(Timestamp.from(Instant.now()));
            zenodoAssessmentInfo.setIsPublished(Boolean.TRUE);

            return ZenodoAssessmentInfoMapper.INSTANCE.zenodoAssessmentInfoToResponse(zenodoAssessmentInfo);

        } catch (Exception e) {
            // Rollback: If upload failed, delete deposit from Zenodo
            if (depositId != null) {
                try {
                    zenodoClient.deleteDeposit(ACCESS_TOKEN, String.valueOf(depositId));
                } catch (Exception rollbackEx) {
                    // Log rollback failure but don't override the original exception
                    throw new RuntimeException("Failed to rollback Zenodo deposit: " + depositId);
                }
            }
            throw new RuntimeException("Failed to publish assessment: " + assessmentId, e);
        }
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
        Map<String, Object> response = zenodoClient.uploadFile(ACCESS_TOKEN, depositionId, requestBody, "multipart/form-data; boundary=" + boundary);
        // Check the response and handle accordingly
        if (response.containsKey("id")) {
            Log.info("File uploaded successfully: " + response);
        } else {
            Log.info("Upload failed with response: " + response);
        }

        // Delete the file after upload (optional)
        fileContent.delete();
    }

    public Map<String, Object> createDeposit(String ACCESS_TOKEN, Map<String, Object> metadata) {
        try {
            Map<String, Object> deposit = zenodoClient.createDeposit(ACCESS_TOKEN, metadata);
            return deposit;
        } catch (WebApplicationException e) {
            throw new WebApplicationException(e.getMessage());
        } catch (ProcessingException e) {
            throw new ProcessingException(e.getMessage());
        }
    }

    @Transactional
    public String publishDepositToZenodo(String depositId) {


        var assessmentOpt = zenodoAssessmentInfoRepository.getAssessmentByDepositId(depositId);
        if (assessmentOpt.isEmpty()) {
            throw new RuntimeException(" Deposit with ID:  " + depositId + " is not found in CAT Service");
        } else {
            if (assessmentOpt.get().getIsPublished().equals(Boolean.TRUE)) {
                throw new RuntimeException(" Deposit with ID:  " + depositId + " is already published");

            }
        }
        publishDeposit(ACCESS_TOKEN, depositId);
        var assessment = assessmentOpt.get();
        assessment.setPublishedAt(Timestamp.from(Instant.now()));
        assessment.setIsPublished(Boolean.TRUE);
        zenodoAssessmentInfoRepository.persist(assessment);


        return "Deposit with ID : " + depositId + " published successfully!";
    }

    public Map<String, Object> publishDeposit(String ACCESS_TOKEN, String depositId) {
        try {
            var response = zenodoClient.publishDeposit(ACCESS_TOKEN, depositId);
            return response;
        } catch (WebApplicationException e) {
            throw new WebApplicationException(e.getMessage());
        } catch (ProcessingException e) {
            throw new ProcessingException(e.getMessage());
        }
    }

    private Map<String, Object> createMetadata(MotivationAssessment assessment) {
        Map<String, Object> metadata = new HashMap<>();

        var dbAssessmentToJson = AssessmentMapper.INSTANCE.userRegistryAssessmentToJsonAssessment(assessment);

        String title = generateTitle(dbAssessmentToJson);
        String description = "Publishing assessment " + dbAssessmentToJson.assessmentDoc.name + " to Zenodo";
        String uploadType = "dataset";

        String userId = dbAssessmentToJson.getUserId();
        var user = userRepository.fetchUser(userId);

        List<Map<String, String>> creators = new ArrayList<>();
        List<Map<String, String>> contributors = new ArrayList<>();

        // Add creator to the list
        creators.add(createCreatorOrContributorMap(user));

        // Add contributors (excluding the main user)
        List<String> sharedUserIds = keycloakAdminService.getIdsOfSharedUsers(
                ShareableEntityType.ASSESSMENT.getValue().concat(ENTITLEMENTS_DELIMITER).concat(assessment.getId())
        );
        var dbUsers = userRepository.fetchUsers(sharedUserIds);
        dbUsers.stream()
                .filter(dbUser -> !dbUser.getId().equals(user.getId())) // Exclude main user
                .forEach(dbUser -> contributors.add(createCreatorOrContributorMap(dbUser, "editor")));

        // Prepare metadata map
        metadata.put("metadata", Map.of(
                "title", title,
                "upload_type", uploadType,
                "description", description,
                "creators", creators,
                "contributors", contributors
        ));

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
