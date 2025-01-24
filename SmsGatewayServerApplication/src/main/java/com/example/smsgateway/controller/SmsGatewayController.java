//package com.example.smsgateway.controller;
//
//import com.example.smsgateway.service.SmsGatewayService;
//import org.springframework.stereotype.Controller;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.net.ServerSocket;
//import java.net.Socket;
//
//@Controller
//public class SmsGatewayController {
//
//    private final SmsGatewayService service;
//
//    public SmsGatewayController(SmsGatewayService service) {
//        this.service = service;
//        startGatewayServer();
//    }
//
//    private void startGatewayServer() {
//        new Thread(() -> {
//            try (ServerSocket serverSocket = new ServerSocket(8082)) {
//                System.out.println("Gateway Server running on port 8082...");
//                while (true) {
//                    try (Socket clientSocket = serverSocket.accept();
//                         BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
//
//                        String message;
//                        while ((message = reader.readLine()) != null) {
//                            processMessage(message);
//                        }
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();
//    }
//
//    private void processMessage(String message) {
//        try {
//            String[] parts = message.split(",");
//            Long id = Long.parseLong(parts[0]);
//            String email = parts[1];
//            String text = parts[2];
//
//            boolean isSent = service.sendEmail(email, text);
//            String status = isSent ? "SENT" : "ERROR";
//
//            // Update the status back in the Receiving Server
//            try (Socket socket = new Socket("localhost", 8081)) {
//                String response = id + "," + status + "\n";
//                socket.getOutputStream().write(response.getBytes());
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
//
