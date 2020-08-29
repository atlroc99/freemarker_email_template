package com.jeffry.notification;

import com.jeffry.notification.Dto.MailRequest;
import com.jeffry.notification.Dto.MailResponse;
import com.jeffry.notification.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@RestController
public class NotificationApplication {

    @Autowired
    private EmailService emailService;

    @PostMapping("/notification")
    public ResponseEntity<MailResponse> sendEmail(@RequestBody MailRequest mailRequest) {
        Map<String, Object> model = new HashMap();
        model.put("Name", mailRequest.getName());
        model.put("location", "Atlanta, GA");
        MailResponse mailResponse = emailService.sendEmail(mailRequest, model);

        return new ResponseEntity<>(mailResponse, HttpStatus.OK);
    }

	public static void main(String[] args) {
		SpringApplication.run(NotificationApplication.class, args);
	}

}
