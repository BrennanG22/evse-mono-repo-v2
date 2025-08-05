package com.brennan.pigpio;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

public class RFIDChecker {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static boolean isCardPresent() {
        boolean cardDetected = false;
        String apiUrl = "http://localhost:5000/read_card";

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Set timeout (in milliseconds)
            connection.setConnectTimeout(3000); // 3 seconds
            connection.setReadTimeout(7000);    // 7 seconds

            int responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                // Read response into StringBuilder
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = in.readLine()) != null) {
                    response.append(line);
                }

                in.close();

                // Parse JSON with Jackson
                JsonNode json = objectMapper.readTree(response.toString());
                String status = json.path("status").asText();

                if ("success".equalsIgnoreCase(status)) {
                    cardDetected = true;
                    System.out.println("Card detected! UID: " + json.path("uid").asText());
                } else {
                    System.out.println("API responded but no card detected: " + status);
                }

            } else {
                System.out.println("API returned non-OK status: " + responseCode);
            }

        } catch (SocketTimeoutException e) {
            System.out.println("Request timed out: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Exception while calling RFID API: " + e.getMessage());
        }

        return cardDetected;
    }
}
