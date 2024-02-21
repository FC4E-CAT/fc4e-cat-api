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
    private String uiBaseUrl;


    @Inject
    KeycloakAdminRepository keycloakAdminRepository;

    public List<String> retrieveAdminEmails() {

        ArrayList<String> vops = new ArrayList<>();
        var admins = keycloakAdminRepository.fetchRolesMembers("admin");
        admins.stream().map(admin -> admin.getAttributes().get("voperson_id")).forEach(vops::addAll);
        return vops.stream().map(person -> userRepository.fetchUser(person).getEmail()).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public void sendMails(Validation val, MailType type, List<String> mailAddrs) {
        switch (type) {
            case ADMIN_ALERT_NEW_VALIDATION:
                notifyAdmins(val, mailAddrs);
                break;
            case VALIDATED_ALERT_CHANGE_VALIDATION_STATUS:
                notifyUser(val, mailAddrs);
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

        var mail = buildEmail(templateParams, MailType.VALIDATED_ALERT_CHANGE_VALIDATION_STATUS, validation.getUser().getEmail());
        mailer.send(mail);
    }

    private void notifyAdmins(Validation validation, List<String> mailAddrs) {

        HashMap<String, String> templateParams = new HashMap();
        templateParams.put("valUrl", uiBaseUrl + "/admin/validations/" + validation.getId());

        var mail = buildEmail(templateParams, MailType.ADMIN_ALERT_NEW_VALIDATION, String.valueOf(Arrays.asList(mailAddrs)));
        mailer.send(mail);
    }
    public static class CustomCompletableFuture<T> extends CompletableFuture<T> {
        static final Executor EXEC = Executors.newFixedThreadPool(10,
                runnable -> new Thread(runnable)
        );

        @Override
        public Executor defaultExecutor() {
            return EXEC;

        }

        @Override
        public <U> CompletableFuture<U> newIncompleteFuture() {
            return new CustomCompletableFuture<>();
        }


        public static CompletableFuture<Void> runAsync(Runnable runnable) {
            Objects.requireNonNull(runnable);
            return supplyAsync(() -> {
                runnable.run();
                return null;
            });
        }

        public static <U> CompletableFuture<U> supplyAsync​(Supplier<U> supplier) {
            return new CustomCompletableFuture<U>().completeAsync(supplier);
        }
    }

}

