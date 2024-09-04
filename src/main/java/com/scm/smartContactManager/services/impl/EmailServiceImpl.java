package com.scm.smartContactManager.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.scm.smartContactManager.services.EmailService;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender eMailSender;

    @Value("${spring.mail.properties.domain_name}")
    private String domainName;

    @Override
    public void sendEmail(String recipient, String subject, String body) {

        MimeMessage mimeMessage = eMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(recipient);
            helper.setSubject(subject);
            helper.setText(body);
            helper.setFrom(domainName);

            eMailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
