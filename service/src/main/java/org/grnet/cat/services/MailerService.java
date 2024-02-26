package org.grnet.cat.services;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.grnet.cat.entities.Validation;
import org.grnet.cat.enums.MailType;
import org.grnet.cat.repositories.KeycloakAdminRepository;
import org.grnet.cat.repositories.UserRepository;
import org.jboss.logging.Logger;
import org.keycloak.representations.idm.UserRepresentation;

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

    private static final Logger LOG = Logger.getLogger(MailerService.class);

    @Inject
    KeycloakAdminRepository keycloakAdminRepository;

    @ConfigProperty(name = "keycloak.admin-client.search.user.by.attribute")
    String attribute;

    public List<String> retrieveAdminEmails() {

        LOG.info("Retrieve emails:");
        LOG.info("Retrieve emails Active Threads:" + Thread.activeCount());
        ArrayList<String> vops = new ArrayList<>();
        var admins = keycloakAdminRepository.fetchRolesMembers("admin");
        LOG.info("Fetch admins successful " + admins.size());

        List<String> emails = new ArrayList<>();
        for (UserRepresentation ur : admins) {
            for (String key : ur.getAttributes().keySet()) {
                LOG.info("user representation key: " + key + " has value : " + ur.getAttributes().get(key));
            }
            List<String> voips = ur.getAttributes().get(attribute);
            if (voips == null) {
                LOG.info("null voips --");
                emails.add("cthermolia@gmail.com");
            } else {
                for (String voip : voips) {
                    LOG.info("VOIP: " + voip);
                    var user = userRepository.fetchUser(voip);
                    if (user.getEmail() != null) {
                        emails.add(user.getEmail());
                    }
                }
            }

        }
        return emails;
        //        admins.stream().map(admin -> admin.getAttributes().get("voperson_id")).forEach(vops::addAll);
        //      return vops.stream().map(person -> userRepository.fetchUser(person).getEmail()).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public void sendMails(Validation val, MailType type, List<String> mailAddrs) {
        LOG.info("SEND MAILS - CURRENT THREAD " + Thread.currentThread().getName());
        LOG.info("Send emails Active Threads:" + Thread.activeCount());
        switch (type) {
            case ADMIN_ALERT_NEW_VALIDATION:
                LOG.info("Notify Admins: " + Arrays.toString(mailAddrs.toArray()) + " for validation " + val.getId());
                notifyAdmins(val, mailAddrs);
                break;
            case VALIDATED_ALERT_CHANGE_VALIDATION_STATUS:
                LOG.info("Notify User: " + val.getUser().getEmail() + " for validation " + val.getId());

                var addrs = new ArrayList<String>();
                addrs.add(val.getUser().getEmail());
                notifyUser(val, addrs);
                break;
            default:
                break;

        }

    }

    public Mail buildEmail(HashMap<String, String> templateParams, MailType mailType, String... to) {
        MailType.MailTemplate mailTemplate = mailType.execute(templateParams);

        Mail mail = new Mail();
        mail.addTo(to);
        mail.setText(mailTemplate.getBody());
        mail.setSubject(mailTemplate.getSubject());
        return mail;
    }

    private void notifyUser(Validation validation, List<String> mailAddrs) {
        HashMap<String, String> templateParams = new HashMap();
        templateParams.put("valUrl", uiBaseUrl + "/validations/" + validation.getId());
        templateParams.put("status", validation.getStatus().name());

        //  var mail = buildEmail(templateParams, MailType.VALIDATED_ALERT_CHANGE_VALIDATION_STATUS, String.valueOf(Arrays.asList(mailAddrs)));

        for (String emailAddr : mailAddrs) {
            var mail = buildEmail(templateParams, MailType.VALIDATED_ALERT_CHANGE_VALIDATION_STATUS, emailAddr);
            try {
                mailer.send(mail);
            } catch (Exception e) {

                LOG.error("Cannot send the email because of : " + e.getMessage());
            }
        }
    }

    private void notifyAdmins(Validation validation, List<String> mailAddrs) {

        HashMap<String, String> templateParams = new HashMap();
        templateParams.put("valUrl", uiBaseUrl + "/admin/validations/" + validation.getId());

        for (String emailAddr : mailAddrs) {
            var mail = buildEmail(templateParams, MailType.ADMIN_ALERT_NEW_VALIDATION, emailAddr);
            try {
                LOG.info("EMAIL INFO " + "from: " + mail.getFrom() + " to: " + Arrays.toString(mail.getTo().toArray()) + " subject: " + mail.getSubject() + " message:" + mail.getText());
                mailer.send(mail);
            } catch (Exception e) {

                LOG.error("Cannot send the email because of : " + e.getMessage());
            }
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
                LOG.info("Running on thread: " + Thread.currentThread().getName());
                runnable.run();
                return null;
            });
        }

        public static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier) {
            LOG.info("Syncing on thread: " + Thread.currentThread().getName());
            LOG.info("Active Threads: " + Thread.activeCount());

            return new CompletableFuture<U>().completeAsync(supplier);
        }
    }

}

