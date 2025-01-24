package com.example.smsreceiving.service;

import com.example.smsreceiving.LogFile;
import com.example.smsreceiving.model.EmailRecord;
import com.example.smsreceiving.repository.EmailRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class SmsReceivingService {
    private final EmailRecordRepository repository;
    private static final String GATEWAY_SERVER_URL = "http://127.0.0.1:8086/api/gateway/process";

    public SmsReceivingService(EmailRecordRepository repository) {
        this.repository = repository;
    }

    public void processRecords() throws Exception {
        while (true) {
            // Fetch PENDING records
            List<EmailRecord> pendingRecords = repository.findByStatusAndApplicationType("PENDING", "ICBS");
            if (!pendingRecords.isEmpty()) {
                sendToGateway(pendingRecords);
            }

            // Fetch ERROR records
            List<EmailRecord> errorRecords = repository.findByStatusAndApplicationType("ERROR", "ICBS");
            if (!errorRecords.isEmpty()) {
                sendToGateway(errorRecords);
            }

            Thread.sleep(5000); // Delay between processing loops
        }
    }

    private void sendToGateway(List<EmailRecord> records) {
        RestTemplate restTemplate = new RestTemplate();

        for (EmailRecord record : records) {
            String mobileNumber = validateAndFormatMobileNumber(record.getMobileNumber());
            if (mobileNumber == null) {
                LogFile.info("Invalid mobile number for record ID: " + record.getId());
                record.setStatus("INVALID_MOBILE");
                repository.save(record);
                continue;
            }

            String data = record.getId() + "," + record.getMobileNumber() + "," + record.getMessage();
            try {
                String referenceNumber = restTemplate.postForObject(GATEWAY_SERVER_URL, data, String.class);
                System.out.println("Received Reference Number: " + referenceNumber);

                // Update record with the reference number
                record.setReferenceNumber(referenceNumber);
                record.setStatus("PROCESSING");
                repository.save(record);

                LogFile.info("Received Reference Number: " + referenceNumber + " for record ID: " + record.getId());
            } catch (Exception e) {
                System.err.println("Error sending to gateway: " + e.getMessage());
            }
        }
    }

    private String validateAndFormatMobileNumber(String mobileNumber) {
        if (mobileNumber == null) return null;

        mobileNumber = mobileNumber.trim();
        if (mobileNumber.length() == 9 && mobileNumber.startsWith("7") && !mobileNumber.substring(1, 2).equals("9")) {
            return "94" + mobileNumber;
        }
        return null; // Invalid mobile number
    }
}
