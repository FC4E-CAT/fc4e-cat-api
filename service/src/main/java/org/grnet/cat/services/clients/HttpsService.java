package org.grnet.cat.services.clients;

import jakarta.enterprise.context.ApplicationScoped;

import javax.net.ssl.HttpsURLConnection;
import java.net.HttpURLConnection;
import java.net.URL;

@ApplicationScoped
public class HttpsService {

    public static boolean isValidHttpsUrl(String urlString) {
        try {
            // Step 1: Check if the URL starts with "https://" and is well-formed
            if (!urlString.startsWith("https://")) {
                System.out.println("URL must start with https://");
                return false;
            }
            URL url = new URL(urlString);

            // Step 2: Open a connection and ensure it is an HTTPS connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (!(connection instanceof HttpsURLConnection)) {
                System.out.println("The URL is not a secure HTTPS connection.");
                return false;
            }

            // Step 3: Try to connect (without fully reading the response)
            connection.setConnectTimeout(5000); // 5 seconds timeout
            connection.setReadTimeout(5000); // 5 seconds timeout
            connection.setRequestMethod("HEAD"); // We only need the headers to check the connection
            int responseCode = connection.getResponseCode();

            // Check if the response code is a successful one (200-399)
            return responseCode >= 200 && responseCode < 400;
        } catch (Exception e) {
            System.out.println("Failed to connect to the URL: " + e.getMessage());
            return false;
        }
    }

}

