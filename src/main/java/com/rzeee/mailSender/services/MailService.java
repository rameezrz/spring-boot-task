package com.rzeee.mailSender.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String toEmail,
                          String otp){
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom("rzeeebackup@gmail.com");
        mail.setTo(toEmail);
        mail.setSubject("Verify OTP");
        mail.setText("Your OTP is : " + otp);

        mailSender.send(mail);

        System.out.println("Mail sent successfully...");
    }
}
