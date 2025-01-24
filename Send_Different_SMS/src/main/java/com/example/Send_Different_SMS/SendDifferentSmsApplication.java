//package com.example.Send_Different_SMS;
//
//import com.example.Send_Different_SMS.service.SmsService;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//
//@SpringBootApplication
//public class SendDifferentSmsApplication {
//
//	public static void main(String[] args) {
//		SpringApplication.run(SendDifferentSmsApplication.class, args);
//	}
//
//	// Get the SmsService bean and execute the logic
//	SmsService smsService = context.getBean(SmsService.class);
//        smsService.sendMessages();
//
//}

package com.example.Send_Different_SMS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import com.example.Send_Different_SMS.service.SmsService;

@SpringBootApplication
public class SendDifferentSmsApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(SendDifferentSmsApplication.class, args);

		// Get the SmsService bean and execute the logic
		SmsService smsService = context.getBean(SmsService.class);
		smsService.sendMessages();
	}
}

