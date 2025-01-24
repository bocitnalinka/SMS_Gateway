//package com.example.smpcrctimpSmsGatewayServer;
//
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.LinkedList;
//import java.util.Queue;
//
//
//@SpringBootApplication
//@RestController
//@RequestMapping("/api/gateway")
//public class SmpcrctimpSmsGatewayServerApplication {
//
//	private static final Queue<String> recordQueue = new LinkedList<>();
//
//	public static void main(String[] args) {
//		SpringApplication.run(SmpcrctimpSmsGatewayServerApplication.class, args);
//		System.out.println("SMS Gateway Server is running...");
//	}
//
//	@PostMapping("/process")
//	public String processMessage(@RequestBody String message) {
//		System.out.println("Received message from SMS Receiving Server: " + message);
//		recordQueue.add(message);
//
//		// Process the records in the queue
//		if (!recordQueue.isEmpty()) {
//			String record = recordQueue.poll();
//			String referenceNumber = "REF" + System.currentTimeMillis();
//			System.out.println("Generated Reference Number: " + referenceNumber + " for message: " + record);
//			return referenceNumber;
//		}
//		return "No message processed.";
//	}
//
//
//}



package com.example.smpcrctimpSmsGatewayServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@SpringBootApplication
@RestController
@RequestMapping("/api/gateway")
public class SmpcrctimpSmsGatewayServerApplication {

	private static final Queue<String> recordQueue = new LinkedList<>();

	public static void main(String[] args) {
		SpringApplication.run(SmpcrctimpSmsGatewayServerApplication.class, args);
		System.out.println("SMS Gateway Server is running...");
	}

	@PostMapping("/processBulk")
	public List<String> processBulkMessages(@RequestBody List<String> messages) {
		System.out.println("Received bulk messages from SMS Receiving Server: " + messages);
		List<String> referenceNumbers = new ArrayList<>();

		for (String message : messages) {
			recordQueue.add(message);
			String referenceNumber = "REF" + System.currentTimeMillis() + "-" + message.hashCode();
			System.out.println("Generated Reference Number: " + referenceNumber + " for message: " + message);
			referenceNumbers.add(referenceNumber);
		}

		return referenceNumbers;
	}
}

