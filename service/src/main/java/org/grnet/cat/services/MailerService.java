package org.grnet.cat.services;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.grnet.cat.entities.Validation;
import org.grnet.cat.enums.MailType;
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
    @ConfigProperty(name = "ui.base.url")
    String uiBaseUrl;
    @ConfigProperty(name = "service.url")
    String serviceUrl;
    @Inject
    @Location("user_created_validation.html")
    Template userCreatedValitionTemplate;

    @Inject
    @Location("validation_status_updated.html")
    Template validationStatusUpdateTemplate;
    private static final Logger LOG = Logger.getLogger(MailerService.class);

    @Inject
    KeycloakAdminRepository keycloakAdminRepository;

    @ConfigProperty(name = "keycloak.admin-client.search.user.by.attribute")
    String attribute;

    public List<String> retrieveAdminEmails() {
        var admins = keycloakAdminRepository.fetchRolesMembers("admin");
        List<String> emails = new ArrayList<>();
        admins.stream().map(admin -> admin.getAttributes().get(attribute)).forEach(emails::addAll);
        return emails.stream().map(person -> userRepository.fetchUser(person).getEmail()).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public void sendMails(Validation val, MailType type, List<String> mailAddrs) {

        HashMap<String, String> templateParams = new HashMap();
        templateParams.put("status", val.getStatus().name());
        templateParams.put("image",serviceUrl+"/v1/images/logo-grnet.png");

        switch (type) {
            case ADMIN_ALERT_NEW_VALIDATION:
                templateParams.put("valUrl", uiBaseUrl + "/admin/validations/" + val.getId());
                notifyAdmins(userCreatedValitionTemplate, templateParams, mailAddrs);
                break;
            case VALIDATED_ALERT_CHANGE_VALIDATION_STATUS:
                templateParams.put("valUrl", uiBaseUrl + "/validations/" + val.getId());
                notifyUser(validationStatusUpdateTemplate, templateParams, Arrays.asList(val.getUser().getEmail()), type);
                break;
            case VALIDATED_ALERT_CREATE_VALIDATION:
                templateParams.put("valUrl", uiBaseUrl + "/validations/" + val.getId());
                notifyUser(userCreatedValitionTemplate, templateParams, Arrays.asList(val.getUser().getEmail()), type);
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

}

