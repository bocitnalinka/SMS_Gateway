//package com.example.smpcrctimp;
//
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.web.client.RestTemplate;
//
//@SpringBootApplication
//public class SmpcrctimpApplication {
//
//	private static final String RECEIVING_SERVER_URL = "http://127.0.0.1:8089/api/receiving/process";
//
//	public static void main(String[] args) {
//		SpringApplication.run(SmpcrctimpApplication.class, args);
//
//		// Example message
//		String prefix = "QMS";
//		String mobileNumber = "94775060914"; // International format
//		String message = "Welcome to BOC SMS Gateway.";
//		String formattedMessage = prefix + mobileNumber + "|" + message;
//
//		RestTemplate restTemplate = new RestTemplate();
//		try {
//			String response = restTemplate.postForObject(RECEIVING_SERVER_URL, formattedMessage, String.class);
//			System.out.println("Response from SMS Receiving Server: " + response);
//		} catch (Exception e) {
//			System.err.println("Error sending message: " + e.getMessage());
//		}
//	}
//}


package com.example.smpcrctimp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class SmpcrctimpApplication {

	private static final String RECEIVING_SERVER_URL = "http://127.0.0.1:8089/api/receiving/processBulk";

	public static void main(String[] args) {
		SpringApplication.run(SmpcrctimpApplication.class, args);

		// Example bulk messages
		List<String> messages = Arrays.asList(
				"QMS+94775060914|Welcome to BOC SMS Gateway.",
				"QMS+94771234567|Your OTP is 123456.",
				"QMS+94776543210|Thank you for using our service."
		);

		RestTemplate restTemplate = new RestTemplate();
		try {
			ResponseEntity<String> response = restTemplate.postForEntity(RECEIVING_SERVER_URL, messages, String.class);
			System.out.println("Response from SMS Receiving Server: " + response.getBody());
		} catch (Exception e) {
			System.err.println("Error sending messages: " + e.getMessage());
		}
	}
}
