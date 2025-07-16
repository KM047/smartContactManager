package com.scm.smartContactManager.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.scm.smartContactManager.services.EmailService;

import io.mailtrap.client.MailtrapClient;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.emails.Address;
import io.mailtrap.model.request.emails.MailtrapMail;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender eMailSender;

    @Value("${spring.mail.properties.token}")
    private static String TOKEN;

    @Override
    public void sendEmail(String recipient, String subject, String body) {

        MailtrapConfig config = new MailtrapConfig.Builder()
                .token(TOKEN)
                .build();

        MailtrapClient client = MailtrapClientFactory.createMailtrapClient(config);
        try {
            // MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            // helper.setTo(recipient);
            // helper.setSubject(subject);
            // helper.setText(body);
            // helper.setFrom(domainName);
            // eMailSender.send(mimeMessage);
            MailtrapMail mail = MailtrapMail.builder()
                    .from(new Address("sender@domain.com"))
                    .to(List.of(new Address(recipient)))
                    .subject(subject)
                    .text(body)
                    .build();

            client.send(mail);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
