package org.grnet.cat.enums;

import io.quarkus.qute.Template;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public enum MailType {


    ADMIN_ALERT_NEW_VALIDATION() {
        public MailTemplate execute(Template emailTemplate, HashMap<String, String> templateParams) {

            URL url;
            String urlString = templateParams.get("valUrl");
            try {
                url = new URL(urlString);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }

            String body = emailTemplate.data("urlpath", url.toString())
                    .data("image", templateParams.get("image"))
                    .data("image1", templateParams.get("image1"))
                    .data("image2", templateParams.get("image2"))
                    .data("image3", templateParams.get("image3"))
                    .data("image4", templateParams.get("image4"))
                    .data("cat", templateParams.get("cat"))
                    .data("userrole", templateParams.get("userrole"))
                    .data("contactMail", templateParams.get("contactMail"))

                    .render();

            return new MailTemplate("Validation Request Created", body);
        }
    },

    VALIDATED_ALERT_CHANGE_VALIDATION_STATUS {
        public MailTemplate execute(Template emailTemplate, HashMap<String, String> templateParams) {
            URL url;
            String urlString = templateParams.get("valUrl");
            try {
                url = new URL(urlString);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }

            String body = emailTemplate.data("urlpath", url.toString())
                    .data("image", templateParams.get("image"))
                    .data("image1", templateParams.get("image1"))
                    .data("image2", templateParams.get("image2"))
                    .data("image3", templateParams.get("image3"))
                    .data("image4", templateParams.get("image4"))
                    .data("cat", templateParams.get("cat"))
                    .data("userrole", templateParams.get("userrole"))
                    .data("contactMail", templateParams.get("contactMail"))
                    .data("status", templateParams.get("status")).render();
            return new MailTemplate("Validation Request updated status", body);
        }

    },
    VALIDATED_ALERT_CREATE_VALIDATION {
        public MailTemplate execute(Template emailTemplate, HashMap<String, String> templateParams) {
            URL url;
            String urlString = templateParams.get("valUrl");
            try {
                url = new URL(urlString);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }

            String body = emailTemplate.data("urlpath", url.toString())
                    .data("image", templateParams.get("image"))
                    .data("image1", templateParams.get("image1"))
                    .data("image2", templateParams.get("image2"))
                    .data("image3", templateParams.get("image3"))
                    .data("image4", templateParams.get("image4"))
                    .data("cat", templateParams.get("cat"))
                    .data("userrole", templateParams.get("userrole"))
                    .data("contactMail", templateParams.get("contactMail"))
                    .render();
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
