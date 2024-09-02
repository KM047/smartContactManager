package com.scm.smartContactManager.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scm.smartContactManager.models.Contact;
import com.scm.smartContactManager.services.ContactService;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private ContactService contactService;

    @RequestMapping("/test")
    public String testApi() {
        return "API Test Successful!";
    }

    @GetMapping("/contact/{contactId}")
    public Contact getContact(@PathVariable String contactId) {

        return contactService.getContactById(contactId);
    }

}
