package org.grnet.cat.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.BadRequestException;
import org.grnet.cat.dtos.AutomatedCheckResponse;
import org.grnet.cat.dtos.AutomatedCheckRequest;
import org.jboss.logging.Logger;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

@ApplicationScoped
public class AutomatedCheckService {

    private static final Logger LOG = Logger.getLogger(org.grnet.cat.services.AutomatedCheckService.class);

    /**
     * Checks if a url is a valid https url.
     * @return AutomatedTestDto if url is a valid http check else an exception
     */
    public AutomatedCheckResponse isValidHttpsUrl(AutomatedCheckRequest urlRequest) {
        try {
            URL url = new URL(urlRequest.url);

            // Open a connection and ensure it is an HTTPS connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (!(connection instanceof HttpsURLConnection)) {
                throw new ConnectException("Failed to connect to the URL: " + url + ". The URL is not a secure HTTPS connection.");
            }

            // Try to connect (without fully reading the response)
            connection.setConnectTimeout(5000); // 5 seconds timeout
            connection.setReadTimeout(5000); // 5 seconds timeout
            connection.setRequestMethod("HEAD"); // Only need headers to check connection

            try {
                int responseCode = connection.getResponseCode(); // This line can throw IOException

                // Return true only if response code is 200 (OK)
                if (responseCode == 200) {
                    var response = new AutomatedCheckResponse();
                    response.code = responseCode;
                    response.isValidHttps = true;
                    return response;
                } else {
                    throw new BadRequestException("Failed to connect to the URL: " + url + ". Received status code " + responseCode);
                }
            } catch (IOException ioException) {
                throw new BadRequestException("Failed to connect to the URL: " + url + ". IOException: " + ioException.getMessage());
            }

        } catch (Exception e) {
            // If any exception occurs, wrap it in a BadRequestException with a message
            throw new BadRequestException(e.getMessage());
        }
    }

}