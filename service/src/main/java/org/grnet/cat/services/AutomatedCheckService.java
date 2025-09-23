package org.grnet.cat.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.BadRequestException;
import org.grnet.cat.dtos.AutomatedCheckRequest;
import org.grnet.cat.dtos.AutomatedTestResponse;
import org.grnet.cat.dtos.AutomatedTestStatus;
import org.jboss.logging.Logger;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@ApplicationScoped
public class AutomatedCheckService {

    private static final Logger LOG = Logger.getLogger(org.grnet.cat.services.AutomatedCheckService.class);

    /**
     * Checks if a url is a valid https url.
     *
     * @return AutomatedTestDto if url is a valid http check else an exception
     */

    public AutomatedTestResponse isValidHttpsUrl(AutomatedCheckRequest urlRequest) {

        var response = new AutomatedTestResponse();

        var status = new AutomatedTestStatus();

        response.testStatus = status;

        try {
            var url = new URL(urlRequest.url);

            var connection = (HttpURLConnection) url.openConnection();
            if (!(connection instanceof HttpsURLConnection)) {
                status.code = 503;
                status.isValid = false;
                status.message = "Failed to connect to the URL: " + url + ". The URL is not a secure HTTPS connection.";
                return response;
            }

            // Try to connect (without fully reading the response)
            connection.setConnectTimeout(5000); // 5 seconds timeout
            connection.setReadTimeout(5000); // 5 seconds timeout
            connection.setRequestMethod("HEAD"); // Only need headers to check connection

            try {
                var responseCode = connection.getResponseCode();

                if (responseCode == 200) {

                    status.isValid = true;
                    status.message = "Valid https url: " + url;
                } else {

                    status.isValid = false;
                    status.message = "Failed to connect to the URL: " + url + ". Received status code " + responseCode;

                }
            } catch (IOException ioException) {

                throw new BadRequestException("Failed to connect to the URL: " + url + ". IOException: " + ioException.getMessage());
            }

        } catch (Exception e) {

            throw new BadRequestException(e.getMessage());
        }

        return response;
    }
}