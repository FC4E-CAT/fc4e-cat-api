package org.grnet.cat.enums;

import io.quarkus.qute.Template;
import jakarta.inject.Inject;
import okhttp3.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public enum MailType {


    ADMIN_ALERT_NEW_VALIDATION() {
        public MailTemplate execute(Template emailTemplate, HashMap<String, String> templateParams) {
            URL url;
            String urlString = templateParams.get("valUrl");
            String body = "";
            try {
                System.out.println("testign template");
                url = new URL(urlString);
                body = emailTemplate.data("urlpath", url.toString()).data("image",templateParams.get("image")).render();
                System.out.println("body is : "+body);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            return new MailTemplate("Validation Request Created", body);
        }
    },

    VALIDATED_ALERT_CHANGE_VALIDATION_STATUS {
        public MailTemplate execute(Template emailTemplate, HashMap<String, String> templateParams) {
            URL url;
            String urlString = templateParams.get("valUrl");
            String body = "";
            try {
                url = new URL(urlString);
                body = emailTemplate.data("urlpath", url.toString()).data("status", templateParams.get("status")).data("image",templateParams.get("image")).render();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            return new MailTemplate("Validation Request updated status", body);
        }

    },
    VALIDATED_ALERT_CREATE_VALIDATION {
        public MailTemplate execute(Template emailTemplate, HashMap<String, String> templateParams) {
            URL url;

            String urlString = templateParams.get("valUrl");
            String body = "";
            try {
                url = new URL(urlString);
                body = emailTemplate.data("urlpath", url.toString()).data("image",templateParams.get("image")).render();
                System.out.println("body*** " +body);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            return new MailTemplate("Validation Request Created", body);
        }

    };

    public abstract MailTemplate execute(Template mailTemplate, HashMap<String, String> templateParams);


    public class MailTemplate {
        String subject;
        String body;

        public MailTemplate(String subject, String body) {
            this.subject = subject;
            this.body = body;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }
    }

}
