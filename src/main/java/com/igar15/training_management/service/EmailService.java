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
        message.setSubject(environment.getProperty("email.verification.message.subject"));
        message.setText(String.format(environment.getProperty("email.verification.message.text") , name, url));
        message.setFrom(environment.getProperty("support.email"));
        mailSender.send(message);
    }

    public void sendPasswordResetEmail(String name, String email, String passwordResetToken) {
        // url below must contain url to frontend password reset functional
        final String url = "http://localhost:8081/training-management-frontend" + "/users/password-reset?token=" + passwordResetToken;
        final SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(environment.getProperty("email.passwordreset.message.subject"));
        message.setText(String.format(environment.getProperty("email.passwordreset.message.text") , name, url));
        message.setFrom(environment.getProperty("support.email"));
        mailSender.send(message);
    }
}
