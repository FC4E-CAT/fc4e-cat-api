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
                    .data("valId", templateParams.get("valId"))
                    .data("image", templateParams.get("image"))
                    .data("image1", templateParams.get("image1"))
                    .data("image2", templateParams.get("image2"))
                    .data("image3", templateParams.get("image3"))
                    .data("image4", templateParams.get("image4"))
                    .data("cat", templateParams.get("cat"))
                    .data("userrole", templateParams.get("userrole"))
                    .data("contactMail", templateParams.get("contactMail"))

                    .render();

            return new MailTemplate("["+templateParams.get("title")+"] - Validation Request Created with id: "+templateParams.get("valId"), body);
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

            //String rejectionReason = templateParams.get("rejectionReason");
            var rejectionReason = "";

            if ("REJECTED".equalsIgnoreCase(templateParams.get("status"))) {
                rejectionReason = "Reason for Rejection: " + (templateParams.get("rejectionReason") != null
                        ? templateParams.get("rejectionReason") : "N/A");
            }

            String body = emailTemplate.data("urlpath", url.toString())
                    .data("valId", templateParams.get("valId"))
                    .data("image", templateParams.get("image"))
                    .data("image1", templateParams.get("image1"))
                    .data("image2", templateParams.get("image2"))
                    .data("image3", templateParams.get("image3"))
                    .data("image4", templateParams.get("image4"))
                    .data("cat", templateParams.get("cat"))
                    .data("userrole", templateParams.get("userrole"))
                    .data("contactMail", templateParams.get("contactMail"))
                    .data("status", templateParams.get("status"))
                    .data("rejectionReason", rejectionReason)
                    .data("actor", templateParams.get("actor"))
                    .data("organization", templateParams.get("organization"))
                    .render();
            return new MailTemplate("["+templateParams.get("title")+"] - Validation Request updated status with id: "+templateParams.get("valId"), body);
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
                    .data("valId", templateParams.get("valId"))
                    .data("image", templateParams.get("image"))
                    .data("image1", templateParams.get("image1"))
                    .data("image2", templateParams.get("image2"))
                    .data("image3", templateParams.get("image3"))
                    .data("image4", templateParams.get("image4"))
                    .data("cat", templateParams.get("cat"))
                    .data("userrole", templateParams.get("userrole"))
                    .data("contactMail", templateParams.get("contactMail"))
                    .data("valId", templateParams.get("valId"))
                    .render();
            return new MailTemplate("["+templateParams.get("title")+"] - Validation Request Created with id: "+templateParams.get("valId"), body);
        }

    },
    USER_ALERT_SHARED_ASSESSMENT {
        public MailTemplate execute(Template emailTemplate, HashMap<String, String> templateParams) {

            URL url;
            String urlString = templateParams.get("assessmentUrl");
            try {
                url = new URL(urlString);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }

            String body = emailTemplate.data("urlpath", url.toString())
                    .data("assessmentUrl", templateParams.get("assessmentUrl"))
                    .data("assessmentName", templateParams.get("assessmentName"))
                    .data("assessmentId", templateParams.get("assessmentId"))
                    .data("image", templateParams.get("image"))
                    .data("image1", templateParams.get("image1"))
                    .data("image2", templateParams.get("image2"))
                    .data("image3", templateParams.get("image3"))
                    .data("image4", templateParams.get("image4"))
                    .data("cat", templateParams.get("cat"))
                    .data("userrole", templateParams.get("userrole"))
                    .data("name", templateParams.get("name"))
                    .data("contactMail", templateParams.get("contactMail"))
                    .render();

            return new MailTemplate("[" + templateParams.get("title") + "] - Shared Assessment with name: " + templateParams.get("assessmentName"), body);
        }
        },
    ZENODO_COMPLETED_PUBLISH_PROCESS {
        public MailTemplate execute(Template emailTemplate, HashMap<String, String> templateParams) {

            URL url;
            String urlString = templateParams.get("depositUrl");
            try {
                url = new URL(urlString);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }

            String body = emailTemplate.data("depositUrl", url.toString())
                    .data("depositId", templateParams.get("depositId"))
                    .data("assessmentName", templateParams.get("assessmentName"))

                    .data("assessmentId", templateParams.get("assessmentId"))
                    .data("image", templateParams.get("image"))
                    .data("image1", templateParams.get("image1"))
                    .data("image2", templateParams.get("image2"))
                    .data("image3", templateParams.get("image3"))
                    .data("image4", templateParams.get("image4"))
                    .data("name",templateParams.get("name"))
                    .data("contactMail", templateParams.get("contactMail"))
                    .render();

            return new MailTemplate("[" + templateParams.get("title") + "] - Completed Process to Publish to Zenodo : " + templateParams.get("assessmentName"), body);
        }
    },
    ZENODO_DRAFT_DEPOSIT {
        public MailTemplate execute(Template emailTemplate, HashMap<String, String> templateParams) {

            URL url;
            String urlString = templateParams.get("depositUrl");
            try {
                url = new URL(urlString);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }

            String body = emailTemplate.data("depositUrl", url.toString())
                    .data("depositId", templateParams.get("depositId"))
                    .data("assessmentName", templateParams.get("assessmentName"))

                    .data("assessmentId", templateParams.get("assessmentId"))
                    .data("image", templateParams.get("image"))
                    .data("image1", templateParams.get("image1"))
                    .data("image2", templateParams.get("image2"))
                    .data("image3", templateParams.get("image3"))
                    .data("image4", templateParams.get("image4"))
                    .data("name",templateParams.get("name"))
                    .data("contactMail", templateParams.get("contactMail"))
                    .render();

            return new MailTemplate("[" + templateParams.get("title") + "] - Uploaded Assessment to Zenodo in draft state " + templateParams.get("assessmentName"), body);
        }
    },
        ZENODO_PUBLISH_ASSESSMENT {
            public MailTemplate execute(Template emailTemplate, HashMap<String, String> templateParams) {

                URL url;
                String urlString = templateParams.get("depositUrl");
                try {
                    url = new URL(urlString);
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }

                String body = emailTemplate.data("depositUrl", url.toString())
                        .data("depositId", templateParams.get("depositId"))
                        .data("assessmentName", templateParams.get("assessmentName"))

                        .data("assessmentId", templateParams.get("assessmentId"))
                        .data("image", templateParams.get("image"))
                        .data("image1", templateParams.get("image1"))
                        .data("image2", templateParams.get("image2"))
                        .data("image3", templateParams.get("image3"))
                        .data("image4", templateParams.get("image4"))
                        .data("name",templateParams.get("name"))
                        .data("contactMail", templateParams.get("contactMail"))
                        .render();

                return new MailTemplate("[" + templateParams.get("title") + "] - Publish to Zenodo : " + templateParams.get("assessmentName"), body);
            }
    },
    ZENODO_PUBLISH_DEPOSIT {
        public MailTemplate execute(Template emailTemplate, HashMap<String, String> templateParams) {

            URL url;
            String urlString = templateParams.get("depositUrl");
            try {
                url = new URL(urlString);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }

            String body = emailTemplate.data("depositUrl", url.toString())
                    .data("depositId", templateParams.get("depositId"))
                    .data("image", templateParams.get("image"))
                    .data("image1", templateParams.get("image1"))
                    .data("image2", templateParams.get("image2"))
                    .data("image3", templateParams.get("image3"))
                    .data("image4", templateParams.get("image4"))
                    .data("name",templateParams.get("name"))
                    .data("contactMail", templateParams.get("contactMail"))
                    .render();

            return new MailTemplate("[" + templateParams.get("title") + "] -  Publish Deposit to Zenodo : " + templateParams.get("depositId"), body);
        }
    },
    ZENODO_FAILED_PUBLISH_PROCESS {
        public MailTemplate execute(Template emailTemplate, HashMap<String, String> templateParams) {


            String body = emailTemplate
                    .data("assessmentName", templateParams.get("assessmentName"))

                    .data("assessmentId", templateParams.get("assessmentId"))
                    .data("image", templateParams.get("image"))
                    .data("image1", templateParams.get("image1"))
                    .data("image2", templateParams.get("image2"))
                    .data("image3", templateParams.get("image3"))
                    .data("image4", templateParams.get("image4"))
                    .data("name",templateParams.get("name"))
                    .data("contactMail", templateParams.get("contactMail"))
                    .render();

            return new MailTemplate("[" + templateParams.get("title") + "] - Failed to Publish to Zenodo : " + templateParams.get("assessmentName"), body);
        }
    },
    ZENODO_FAILED_PUBLISH_DEPOSIT {
        public MailTemplate execute(Template emailTemplate, HashMap<String, String> templateParams) {

            URL url;
            String urlString = templateParams.get("depositUrl");
            try {
                url = new URL(urlString);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }

            String body = emailTemplate.data("depositUrl", url.toString())
                    .data("depositId", templateParams.get("depositId"))
                    .data("image", templateParams.get("image"))
                    .data("image1", templateParams.get("image1"))
                    .data("image2", templateParams.get("image2"))
                    .data("image3", templateParams.get("image3"))
                    .data("image4", templateParams.get("image4"))
                    .data("name",templateParams.get("name"))
                    .data("contactMail", templateParams.get("contactMail"))
                    .render();

            return new MailTemplate("[" + templateParams.get("title") + "] -  Failed to Publish Deposit to Zenodo : " + templateParams.get("depositId"), body);
        }
    },
    ZENODO_PUBLISH_DEPOSIT_DRAFT_IN_DB {
        public MailTemplate execute(Template emailTemplate, HashMap<String, String> templateParams) {

            URL url;
            String urlString = templateParams.get("depositUrl");
            try {
                url = new URL(urlString);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }

            String body = emailTemplate.data("depositUrl", url.toString())
                    .data("depositId", templateParams.get("depositId"))
                    .data("image", templateParams.get("image"))
                    .data("image1", templateParams.get("image1"))
                    .data("image2", templateParams.get("image2"))
                    .data("image3", templateParams.get("image3"))
                    .data("image4", templateParams.get("image4"))
                    .data("name",templateParams.get("name"))
                    .data("contactMail", templateParams.get("contactMail"))
                    .render();

            return new MailTemplate("[" + templateParams.get("title") + "] -  Publish Deposit to Zenodo/Draft db state : " + templateParams.get("depositId"), body);
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
