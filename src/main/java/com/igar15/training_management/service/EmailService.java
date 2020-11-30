package com.igar15.training_management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Environment environment;

    public void sendEmailAddressVerificationEmail(String name, String email, String emailVerificationToken) {
        final String url = "http://localhost:8080/training-management" + "/users/email-verification?token=" + emailVerificationToken;
        final SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Email verifying");
        message.setText("Hello, " + name + "! Please open the following URL to verify your email: \r\n" + url);
        message.setFrom(environment.getProperty("support.email"));
        mailSender.send(message);
    }

    public void sendPasswordResetEmail(String name, String email, String passwordResetToken) {
        final String url = "http://localhost:8080/training-management" + "/users/password-reset?token=" + passwordResetToken;
        final SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Reset password");
        message.setText("Hello, " + name + "! Please open the following URL to reset your password: \r\n" + url);
        message.setFrom(environment.getProperty("support.email"));
        mailSender.send(message);
    }
}
