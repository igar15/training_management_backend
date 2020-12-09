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
        message.setSubject("Email verifying for Training Management App");
        message.setText("Hello, " + name + "! To complete registration in Training Management App please open the following URL to verify your email: \r\n" + url);
        message.setFrom(environment.getProperty("support.email"));
        mailSender.send(message);
    }

    public void sendPasswordResetEmail(String name, String email, String passwordResetToken) {
        // url below must contain url to frontend password reset functional
        final String url = "http://localhost:8081/training-management-frontend" + "/users/password-reset?token=" + passwordResetToken;
        final SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Reset password for Training Management App");
        message.setText("Hello, " + name + "! To reset your password in Training Management App please open the following URL: \r\n" + url);
        message.setFrom(environment.getProperty("support.email"));
        mailSender.send(message);
    }
}
