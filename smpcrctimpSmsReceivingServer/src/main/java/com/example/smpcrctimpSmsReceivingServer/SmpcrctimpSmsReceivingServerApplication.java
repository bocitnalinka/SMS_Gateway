//package com.example.smpcrctimpSmsReceivingServer;
//
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.LinkedList;
//import java.util.Queue;
//
//
//@RestController
//@RequestMapping("/api/receiving")
//@SpringBootApplication
//public class SmpcrctimpSmsReceivingServerApplication {
//
//    private static final Queue<String> messageQueue = new LinkedList<>();
//    private static final String GATEWAY_SERVER_URL = "http://127.0.0.1:8090/api/gateway/process";
//
//    public static void main(String[] args) {
//        SpringApplication.run(SmpcrctimpSmsReceivingServerApplication.class, args);
//        System.out.println("SMS Receiving Server is running...");
//    }
//
//    @PostMapping("/process")
//    public String processIncomingMessage(@RequestBody String message) {
//        System.out.println("Received message from client: " + message);
//        messageQueue.add(message);
//        processMessages();
//        return "Message received and queued for processing.";
//    }
//
//    private void processMessages() {
//        RestTemplate restTemplate = new RestTemplate();
//
//        while (!messageQueue.isEmpty()) {
//            String message = messageQueue.poll();
//            try {
//                String referenceNumber = restTemplate.postForObject(GATEWAY_SERVER_URL, message, String.class);
//                System.out.println("Reference Number received from SMS Gateway: " + referenceNumber + " for message: " + message);
//            } catch (Exception e) {
//                System.err.println("Error sending to SMS Gateway: " + e.getMessage());
//            }
//        }
//    }
//
//
//}




//package com.example.smpcrctimpSmsReceivingServer;
//
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.Map;
//import java.util.Queue;
//
//@SpringBootApplication
//@RestController
//@RequestMapping("/api/receiving")
//public class  SmpcrctimpSmsReceivingServerApplication{
//
//    private static final Queue<String> messageQueue = new LinkedList<>();
//    private static final String GATEWAY_SERVER_URL = "http://127.0.0.1:8090/api/gateway/process";
//    private static final Map<String, String> referenceMap = new HashMap<>();
//
//    public static void main(String[] args) {
//        SpringApplication.run(SmpcrctimpSmsReceivingServerApplication.class, args);
//        System.out.println("SMS Receiving Server is running...");
//    }
//
//    @PostMapping("/process")
//    public String processIncomingMessage(@RequestBody String message) {
//        System.out.println("Received message from client: " + message);
//        messageQueue.add(message);
//        return processMessages(message);
//    }
//
//    private String processMessages(String clientMessage) {
//        RestTemplate restTemplate = new RestTemplate();
//
//        while (!messageQueue.isEmpty()) {
//            String message = messageQueue.poll();
//            try {
//                // Send to SMS Gateway Server
//                String referenceNumber = restTemplate.postForObject(GATEWAY_SERVER_URL, message, String.class);
//                System.out.println("Reference Number received from SMS Gateway: " + referenceNumber + " for message: " + message);
//
//                // Store reference number for the client
//                referenceMap.put(clientMessage, referenceNumber);
//
//                // Return reference number to client
//                return "Message processed. Reference Number: " + referenceNumber;
//            } catch (Exception e) {
//                System.err.println("Error sending to SMS Gateway: " + e.getMessage());
//                return "Error processing message.";
//            }
//        }
//        return "No message processed.";
//    }
//}


package com.example.smpcrctimpSmsReceivingServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

@SpringBootApplication
@RestController
@RequestMapping("/api/receiving")
public class SmpcrctimpSmsReceivingServerApplication {

    private static final Queue<String> messageQueue = new LinkedList<>();
    private static final String GATEWAY_SERVER_URL = "http://127.0.0.1:8090/api/gateway/processBulk";
    private static final Map<String, String> referenceMap = new HashMap<>();

    public static void main(String[] args) {
        SpringApplication.run(SmpcrctimpSmsReceivingServerApplication.class, args);
        System.out.println("SMS Receiving Server is running...");
    }

    @PostMapping("/processBulk")
    public String processIncomingBulkMessages(@RequestBody List<String> messages) {
        System.out.println("Received bulk messages from client: " + messages);
        messageQueue.addAll(messages);
        return processBulkMessages(messages);
    }

//    private String processBulkMessages(List<String> clientMessages) {
//        RestTemplate restTemplate = new RestTemplate();
//        StringBuilder responseSummary = new StringBuilder();
//
//        while (!messageQueue.isEmpty()) {
//            String message = messageQueue.poll();
//            try {
//                // Send bulk messages to SMS Gateway Server
//                String[] response = restTemplate.postForObject(GATEWAY_SERVER_URL, clientMessages, String[].class);
//                for (int i = 0; i < clientMessages.size(); i++) {
//                    String referenceNumber = response[i];
//                    String clientMessage = clientMessages.get(i);
//                    System.out.println("Reference Number received for message: " + clientMessage + " is: " + referenceNumber);
//
//                    // Store reference number for each client message
//                    referenceMap.put(clientMessage, referenceNumber);
//                    responseSummary.append("Message: ").append(clientMessage)
//                            .append(" | Reference: ").append(referenceNumber).append("\n");
//                }
//            } catch (Exception e) {
//                System.err.println("Error processing bulk messages: " + e.getMessage());
//                responseSummary.append("Error processing messages: ").append(e.getMessage()).append("\n");
//            }
//        }
//        return responseSummary.toString();
//    }
//}


    private String processBulkMessages(List<String> clientMessages) {
        RestTemplate restTemplate = new RestTemplate();
        StringBuilder responseSummary = new StringBuilder();

        // Ensure only the received messages are processed
        try {
            // Send bulk messages to SMS Gateway Server
            String[] response = restTemplate.postForObject(GATEWAY_SERVER_URL, clientMessages, String[].class);
            for (int i = 0; i < clientMessages.size(); i++) {
                String referenceNumber = response[i];
                String clientMessage = clientMessages.get(i);
                System.out.println("Reference Number received for message: " + clientMessage + " is: " + referenceNumber);

                // Store reference number for each client message
                referenceMap.put(clientMessage, referenceNumber);
                responseSummary.append("Message: ").append(clientMessage)
                        .append(" | Reference: ").append(referenceNumber).append("\n");
            }
        } catch (Exception e) {
            System.err.println("Error processing bulk messages: " + e.getMessage());
            responseSummary.append("Error processing messages: ").append(e.getMessage()).append("\n");
        }

        return responseSummary.toString();
    }
}