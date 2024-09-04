package com.scm.smartContactManager.services;

public interface EmailService {

    void sendEmail(String recipient, String subject, String body);

}
