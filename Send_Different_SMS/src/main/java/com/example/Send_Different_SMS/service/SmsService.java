//package com.example.Send_Different_SMS.service;
//
//public class SmsService {
//}

package com.example.Send_Different_SMS.service;

import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Service;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

@Service
public class SmsService {

    // Fixed values for client and server details
    private static final String SERVER_IP = "127.0.0.1"; // Replace with your server IP
    private static final int SERVER_PORT = 4141; // Replace with your server port
    private static final String CLIENT_IP = "127.0.0.1"; // Replace with your client IP
    private static final int CLIENT_PORT = 3312; // Replace with your client port (used for logging)


    public void sendMessages() {
        BufferedReader f1;
        BufferedReader f2;
        String mobileno;
        String msgToSend;
        String msg, reply;

        Socket socket = null;
        PrintWriter out = null;
        BufferedReader in = null;

        System.out.println("Send Different SMS Application is running on IP: " + CLIENT_IP + " Port: " + CLIENT_PORT);

        try {
            socket = new Socket(SERVER_IP,SERVER_PORT );
            System.out.println("Connected to SMS Server at " + SERVER_IP + ":" + SERVER_PORT);

            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: server 172.20.50.67");
            return;
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: 172.20.50.67 via port 3312");
            return;
        }

        try {
            f1 = new BufferedReader(new FileReader("mobno.txt"));
            f2 = new BufferedReader(new FileReader("msg.txt"));
            msgToSend = f2.readLine().trim();
            while ((mobileno = f1.readLine().trim()) != null) {

                System.out.print("Msg to:" + mobileno + " -> ");
                if (mobileno.substring(0, 2).equals("94")) {
                    msg = mobileno + msgToSend;
                } else {
                    msg = mobileno + "|" + msgToSend;
                }
                out.println(msg);
                reply = in.readLine();
                System.out.println(reply);
                out.flush();
                Thread.sleep(150); // This is for delaying
            }

            out.println(".");

        } catch (IOException | InterruptedException ex) {
            System.out.println("Error occurred:::" + ex);
        } finally {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                System.out.println("Error while closing resources: " + e.getMessage());
            }
        }
    }
}

