package com.diego.spring.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    
    /**
     * Send email to the seller
     * @param to
     * @param subject
     * @param content
     */
    public void sendEmail(String to, String subject, String content) {
        SimpleMailMessage email = new SimpleMailMessage();
        
        email.setTo(to);
        email.setSubject(subject);
        email.setText(content);
       
        mailSender.send(email);
    }
}
