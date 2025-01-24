//package services;
//
//public class SmsService {
//}
package services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class SmsService {

    private static final String GATEWAY_SERVER_URL = "http://127.0.0.1:8086/api/gateway/process";

    public void sendMessages() {
        BufferedReader fileReader = null;
        String message;
        String msgToSend;

        System.out.println("Send Different SMS Application is running...");

        try {
            // Read from the mobnomsg.txt file
            //fileReader = new BufferedReader(new FileReader("mobno.txt"));
            fileReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/mobno.txt")));
            RestTemplate restTemplate = new RestTemplate();
            int no_sms = 0;

            while ((message = fileReader.readLine()) != null) {
                no_sms++;
                message = message.trim();

                // Add country code if missing
                if ("0".equals(message.substring(0, 1))) {
                    msgToSend = "94" + message.substring(1);
                } else {
                    msgToSend = message;
                }

                System.out.print("Sending message to: " + msgToSend + " -> ");

                // Create payload for HTTP POST request
                Map<String, String> payload = new HashMap<>();
                payload.put("message", msgToSend);

                try {
                    // Send message to SMS Gateway server
                    String response = restTemplate.postForObject(GATEWAY_SERVER_URL, payload, String.class);
                    System.out.println("Response: " + response);
                } catch (Exception e) {
                    System.err.println("Failed to send message: " + e.getMessage());
                }

                // Delay after sending 10 messages
                if (no_sms % 10 == 0) {
                    System.out.println("Batch complete, waiting for 2 seconds...");
                    Thread.sleep(2000);
                }
            }

            // Notify SMS Gateway server of completion
            Map<String, String> payload = new HashMap<>();
            payload.put("message", ".");
            restTemplate.postForObject(GATEWAY_SERVER_URL, payload, String.class);

        } catch (IOException | InterruptedException e) {
            System.err.println("Error occurred: " + e.getMessage());
        } finally {
            // Close resources
            try {
                if (fileReader != null) {
                    fileReader.close();
                }
            } catch (IOException e) {
                System.err.println("Error while closing file: " + e.getMessage());
            }
        }
    }
}

