//package com.example.Send_SMS_List;
//
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//
//@SpringBootApplication
//public class SendSmsListApplication {
//
//	public static void main(String[] args) {
//		SpringApplication.run(SendSmsListApplication.class, args);
//	}
//
//}
package com.example.Send_SMS_List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import services.SmsService;

@SpringBootApplication
public class SendSmsListApplication {

	public static void main(String[] args) {
		SpringApplication.run(SendSmsListApplication.class, args);

		// Call the SmsService to process and send messages
		SmsService smsService = new SmsService();
		smsService.sendMessages();
	}
}

