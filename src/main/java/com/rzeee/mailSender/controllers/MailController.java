package com.rzeee.mailSender.controllers;

import com.rzeee.mailSender.dto.MailRequest;
import com.rzeee.mailSender.services.MailService;
import com.rzeee.mailSender.services.RateLimitService;
import com.rzeee.mailSender.utils.OtpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/mail")
public class MailController {

    @Autowired
    private OtpUtils otpUtils;

    @Autowired
    private MailService mailService;

    @Autowired
    private RateLimitService rateLimitService;

    @PostMapping
    public ResponseEntity<String> sendEmail(@RequestBody MailRequest mailRequest){
        String mailId = mailRequest.getToEmail();
        if(mailId == null || mailId.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email ID is required.");
        }
        if(!rateLimitService.tryConsume(mailId)){
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Too many requests. Please try again later.");
        }
        String otp = otpUtils.generateOtp();
        mailService.sendEmail(mailRequest.getToEmail(), otp);
        return ResponseEntity.ok("Email Sent Successfully...");
    }
}
