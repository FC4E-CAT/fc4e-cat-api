package org.grnet.cat.enums;

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
        public MailTemplate execute(HashMap<String,String> templateParams) {
            URL url;
            String urlString=templateParams.get("valUrl");
            try {
                url=new URL(urlString);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            return new MailTemplate( "New Validation Request", "There is a new validation request: "+url.toString() );
        }
    },

    VALIDATED_ALERT_CHANGE_VALIDATION_STATUS {
        public MailTemplate execute(HashMap<String,String> templateParams) {
            URL url;
            String urlString=templateParams.get("valUrl");
            try {
                url=new URL(urlString);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            return new MailTemplate("Validation Request updated status", "Status of the following validation request is updated to  " + templateParams.get("status") + "\n"+url.toString());
        }

    };
    public abstract MailTemplate execute(HashMap<String,String> templateParams);


   public class MailTemplate {
        String subject;
        String body;

        public MailTemplate( String subject, String body) {
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
