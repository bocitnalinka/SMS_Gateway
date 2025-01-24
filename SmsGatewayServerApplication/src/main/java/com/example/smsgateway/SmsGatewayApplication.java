
package com.example.smsgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.Queue;

@SpringBootApplication
@RestController
@RequestMapping("/api/gateway")
public class SmsGatewayApplication {
	private static final Queue<String> recordQueue = new LinkedList<>();

	public static void main(String[] args) {
		SpringApplication.run(SmsGatewayApplication.class, args);
		System.out.println("SMS Gateway Server is running...");
	}

	@PostMapping("/process")
	public String processMessage(@RequestBody String message) {
		System.out.println("Received from SMS Receiving Server: " + message);
		recordQueue.add(message);

		// Process the records in the queue
		if (!recordQueue.isEmpty()) {
			String record = recordQueue.poll();
			String referenceNumber = "REF" + System.currentTimeMillis();
			System.out.println("Generated Reference Number: " + referenceNumber + " for message: " + record);
			return referenceNumber;
		}
		return "No message processed.";
	}
}
