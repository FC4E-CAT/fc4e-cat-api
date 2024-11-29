package org.grnet.cat.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.grnet.cat.entities.MotivationAssessment;
import org.grnet.cat.entities.Validation;
import org.grnet.cat.enums.MailType;
import org.grnet.cat.enums.ValidationStatus;
import org.grnet.cat.repositories.KeycloakAdminRepository;
import org.grnet.cat.repositories.UserRepository;
import org.jboss.logging.Logger;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * The UserService provides operations for managing User entities.
 */

@ApplicationScoped
@ActivateRequestContext
public class MailerService {
    @Inject
    Mailer mailer;

    @Inject
    UserRepository userRepository;
    @ConfigProperty(name = "api.ui.url")
    String uiBaseUrl;
    @ConfigProperty(name = "api.server.url")
    String serviceUrl;

    @ConfigProperty(name = "quarkus.smallrye-openapi.info-contact-email")
    String contactMail;
    @Inject
    @Location("user_created_validation.html")
    Template userCreatedValitionTemplate;

    @Inject
    @Location("validation_status_updated.html")
    Template validationStatusUpdateTemplate;

    @Inject
    @Location("admin_created_validation.html")
    Template adminCreatedValidationTemplate;


    @Inject
    @Location("shared_assessment_notification")
    Template userSharedAssessmentTemplate;
    private static final Logger LOG = Logger.getLogger(MailerService.class);

    @Inject
    KeycloakAdminRepository keycloakAdminRepository;
    @ConfigProperty(name = "api.keycloak.user.id")
    String attribute;

    @ConfigProperty(name = "api.name")
    String apiName;

    public List<String> retrieveAdminEmails() {

        var admins = keycloakAdminRepository.fetchRolesMembers("admin");
        var vopersonIds = admins.stream().map(admin -> admin.getAttributes().get(attribute)).flatMap(Collection::stream).collect(Collectors.toList());
        var users = vopersonIds.stream().map(person -> userRepository.findByIdOptional(person)).filter(Optional::isPresent).collect(Collectors.toList());
        return users.stream().map(user -> user.get().getEmail()).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public void sendMails(Validation val, MailType type, List<String> mailAddrs) {

        HashMap<String, String> templateParams = new HashMap();
        templateParams.put("contactMail", contactMail);
        templateParams.put("status", val.getStatus().name());
        templateParams.put("image", serviceUrl + "/v1/images/logo.png");
        templateParams.put("image1", serviceUrl + "/v1/images/logo-dans.png");
        templateParams.put("image2", serviceUrl + "/v1/images/logo-grnet.png");
        templateParams.put("image3", serviceUrl + "/v1/images/logo-datacite.png");
        templateParams.put("image4", serviceUrl + "/v1/images/logo-gwdg.png");
        templateParams.put("cat", uiBaseUrl);
        templateParams.put("valId", String.valueOf(val.getId()));
        templateParams.put("title", apiName.toUpperCase());
        templateParams.put("actor", val.getRegistryActor().getLabelActor());
        templateParams.put("organization", val.getOrganisationName());
        templateParams.put("rejectionReason", val.getStatus() == ValidationStatus.REJECTED ? val.getRejectionReason() : null);


        switch (type) {
            case ADMIN_ALERT_NEW_VALIDATION:
                templateParams.put("valUrl", uiBaseUrl + "/admin/validations/" + val.getId());
                templateParams.put("userrole", "Administrator");
                notifyAdmins(adminCreatedValidationTemplate, templateParams, mailAddrs);
                break;
            case VALIDATED_ALERT_CHANGE_VALIDATION_STATUS:
                templateParams.put("valUrl", uiBaseUrl + "/validations/" + val.getId());
                templateParams.put("userrole", val.getUser().getName());
                notifyUser(validationStatusUpdateTemplate, templateParams, Arrays.asList(val.getUser().getEmail()), type);
                break;
            case VALIDATED_ALERT_CREATE_VALIDATION:
                templateParams.put("valUrl", uiBaseUrl + "/validations/" + val.getId());
                templateParams.put("userrole", val.getUser().getName());
                notifyUser(userCreatedValitionTemplate, templateParams, Arrays.asList(val.getUser().getEmail()), type);
                break;
            default:
                break;
        }
    }

    public void sendMails(MotivationAssessment assessment, String name, MailType type, List<String> mailAddrs) {

        HashMap<String, String> templateParams = new HashMap<>();
        templateParams.put("contactMail", contactMail);
        templateParams.put("image", serviceUrl + "/v1/images/logo.png");
        templateParams.put("image1", serviceUrl + "/v1/images/logo-dans.png");
        templateParams.put("image2", serviceUrl + "/v1/images/logo-grnet.png");
        templateParams.put("image3", serviceUrl + "/v1/images/logo-datacite.png");
        templateParams.put("image4", serviceUrl + "/v1/images/logo-gwdg.png");
        templateParams.put("cat", uiBaseUrl);
        templateParams.put("title", apiName.toUpperCase());

        switch (type) {
            case USER_ALERT_SHARED_ASSESSMENT:
                templateParams.put("assessmentUrl", uiBaseUrl + "/assessment/" + assessment.getId());
                templateParams.put("assessmentName", extractAssessmentName(assessment.getAssessmentDoc()));
                templateParams.put("name", name);
                templateParams.put("userrole", "User");
                LOG.info("Template parameters: " + templateParams);
                notifyUser(userSharedAssessmentTemplate, templateParams, mailAddrs, type);
                break;
            default:
                break;
        }
    }



    public Mail buildEmail(Template emailTemplate, HashMap<String, String> templateParams, MailType mailType) {

        MailType.MailTemplate mailTemplate = mailType.execute(emailTemplate, templateParams);
        Mail mail = new Mail();
        mail.setHtml(mailTemplate.getBody());
        mail.setSubject(mailTemplate.getSubject());
        return mail;
    }

    private void notifyUser(Template emailTemplate, HashMap<String, String> templateParams, List<String> mailAddrs, MailType mailType) {


        var mail = buildEmail(emailTemplate, templateParams, mailType);
        mail.setBcc(mailAddrs);

        try {
            mailer.send(mail);
            LOG.info("RECIPIENTS : " + Arrays.toString(mail.getBcc().toArray()));
        } catch (Exception e) {
            LOG.error("Cannot send the email because of : " + e.getMessage());
        }
    }

    private void notifyAdmins(Template emailTemplate, HashMap<String, String> templateParams, List<String> mailAddrs) {

        var mail = buildEmail(emailTemplate, templateParams, MailType.ADMIN_ALERT_NEW_VALIDATION);
        mail.setBcc(mailAddrs);
        try {
            LOG.info("EMAIL INFO " + "from: " + mail.getFrom() + " to: " + Arrays.toString(mail.getTo().toArray()) + " subject: " + mail.getSubject() + " message:" + mail.getText());
            mailer.send(mail);
            LOG.info("RECIPIENTS : " + Arrays.toString(mail.getBcc().toArray()));
        } catch (Exception e) {
            LOG.error("Cannot send the email because of : " + e.getMessage());
        }

    }

    public static class CustomCompletableFuture<T> extends CompletableFuture<T> {
        static final Executor EXEC = Executors.newCachedThreadPool();

        @Override
        public Executor defaultExecutor() {
            return EXEC;

        }

        @Override
        public <U> CompletableFuture<U> newIncompleteFuture() {
            return new CustomCompletableFuture<>();
        }


        public static CompletableFuture<Void> runAsync(Runnable runnable) {
            return supplyAsync(() -> {
                runnable.run();
                return null;
            });
        }

        public static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier) {
            return new CompletableFuture<U>().completeAsync(supplier);
        }
    }

    private String extractAssessmentName(String assessmentDocJson) {
        var mapper = new ObjectMapper();
        String assessmentName = null;

        try {
            var assessmentDocNode = mapper.readTree(assessmentDocJson);
            if (assessmentDocNode.has("name")) {
                assessmentName = assessmentDocNode.get("name").asText();
            } else {
                LOG.warn("The 'name' field was not found in the assessment document.");
            }
        } catch (Exception e) {
            LOG.error("Error parsing assessment document JSON: ", e);
        }

        return assessmentName;
    }


//    private String mapMailTitleByInstance() {
//        String replacement = serviceUrl.replace("https://", "").replace("http://", "").replace("argo.grnet.gr", "").replace(".", " ").replace(":8080","").replace("/","").replace("api","").toUpperCase();
//
//        if(!replacement.contains("CAT")){
//            replacement="CAT ".concat(replacement);
//        }
//        return replacement.toUpperCase();
//    }

}

