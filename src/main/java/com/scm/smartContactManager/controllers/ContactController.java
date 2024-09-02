package com.scm.smartContactManager.controllers;

import java.util.UUID;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.smartContactManager.constants.AppConstants;
import com.scm.smartContactManager.constants.MessageType;
import com.scm.smartContactManager.forms.ContactForm;
import com.scm.smartContactManager.forms.ContactSearchForm;
import com.scm.smartContactManager.helper.Message;
import com.scm.smartContactManager.helper.UserHelper;
import com.scm.smartContactManager.models.Contact;
import com.scm.smartContactManager.models.UserModel;
import com.scm.smartContactManager.services.ContactService;
import com.scm.smartContactManager.services.ImageService;
import com.scm.smartContactManager.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/user/contacts")
public class ContactController {

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private ContactService contactService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/add")
    public String addContactPage(Model model) {

        ContactForm contactForm = new ContactForm();

        model.addAttribute("contactForm", contactForm);
        return "user/add_contact";
    }

    @PostMapping("/add")
    public String saveContact(@Valid @ModelAttribute ContactForm contactForm, BindingResult result,
            Authentication authentication, HttpSession session) {

        logger.info("----------------------------------------------------");
        logger.info("Form submitted: ", contactForm.toString());

        if (result.hasErrors()) {

            result.getAllErrors().forEach(error -> logger.info(error.toString()));

            session.setAttribute("message", Message.builder()
                    .message("Please correct the following errors")
                    .messageType(MessageType.red)
                    .build());
            return "user/add_contact";
        }

        String email = UserHelper.getEmailOfLoggedInUser(authentication);

        UserModel user = userService.getUserByEmail(email);

        Contact contact = new Contact();

        // uploading the image to cloud storage and storing the publicly available url

        logger.info("file information : {}", contactForm.getContactImage().getOriginalFilename());

        logger.info("---------------------file Uploading-------------------------------");

        String fileName = UUID.randomUUID().toString();
        String imageUrl = imageService.uploadImage(contactForm.getContactImage(), fileName);

        logger.info("---------------------file uploaded successfully-------------------------------");
        logger.info("imageUrl: " + imageUrl);

        contact.setName(contactForm.getName());
        contact.setFavorite(contactForm.isFavorite());
        contact.setEmail(contactForm.getEmail());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setAddress(contactForm.getAddress());
        contact.setDescription(contactForm.getDescription());
        contact.setUser(user);
        contact.setWebsiteLink(contactForm.getWebsiteLink());

        contact.setContactImage(imageUrl);
        contact.setCloudinaryImagePublicId(fileName);

        contactService.saveContact(contact);

        System.out.println(contactForm);

        session.setAttribute("msg",
                Message.builder()
                        .message("You have successfully added a new contact")
                        .messageType(MessageType.green)
                        .build());

        return "redirect:/user/contacts/add";
    }

    @RequestMapping
    public String contactPage(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE + "") int size,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction, Model model,
            Authentication authentication) {

        String email = UserHelper.getEmailOfLoggedInUser(authentication);

        UserModel user = userService.getUserByEmail(email);

        Page<Contact> pageContact = contactService.getByUser(user, page, size, sortBy, direction);

        model.addAttribute("pageContact", pageContact);

        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);

        model.addAttribute("contactSearchForm", new ContactSearchForm());

        return "user/contacts";

    }

    @RequestMapping("/search")

    public String searchHandler(
            @ModelAttribute ContactSearchForm contactSearchForm,
            @RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE + "") int size,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            Model model,
            Authentication authentication

    ) {

        var user = userService.getUserByEmail(UserHelper.getEmailOfLoggedInUser(authentication));

        Page<Contact> pageContact = null;

        if (contactSearchForm.getField().equalsIgnoreCase("name")) {

            pageContact = contactService.searchByName(contactSearchForm.getValue(), size, page, sortBy, direction,
                    user);

        } else if (contactSearchForm.getField().equalsIgnoreCase("email")) {

            pageContact = contactService.searchByEmail(contactSearchForm.getValue(), size, page, sortBy, direction,
                    user);

        } else if (contactSearchForm.getField().equalsIgnoreCase("phone")) {

            pageContact = contactService.searchByPhoneNumber(contactSearchForm.getValue(), size, page, sortBy,
                    direction, user);
        }

        model.addAttribute("contactSearchForm", contactSearchForm);

        model.addAttribute("pageContact", pageContact);

        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);

        return "/user/contacts";
    }

    @RequestMapping("/delete/{contactId}")
    public String deleteContact(@PathVariable String contactId, HttpSession session) {

        contactService.deleteContact(contactId);

        session.setAttribute("msg",
                Message.builder()
                        .message("Contact is Deleted successfully !! ")
                        .messageType(MessageType.green)
                        .build());

        return "redirect:/user/contacts";

    }

    @GetMapping("/view/{contactId}")
    public String updateContactFormView(
            @PathVariable("contactId") String contactId,
            Model model) {

        var contact = contactService.getContactById(contactId);
        ContactForm contactForm = new ContactForm();
        contactForm.setName(contact.getName());
        contactForm.setEmail(contact.getEmail());
        contactForm.setPhoneNumber(contact.getPhoneNumber());
        contactForm.setAddress(contact.getAddress());
        contactForm.setDescription(contact.getDescription());
        contactForm.setFavorite(contact.isFavorite());
        contactForm.setWebsiteLink(contact.getWebsiteLink());
        contactForm.setPicture(contact.getContactImage());

        ;
        model.addAttribute("contactForm", contactForm);
        model.addAttribute("contactId", contactId);

        return "user/update_contact";
    }

    @RequestMapping(value = "/update/{contactId}", method = RequestMethod.POST)
    public String updateContactFormPage(@PathVariable String contactId, @Valid @ModelAttribute ContactForm contactForm,
            BindingResult bindingResult, HttpSession session) {

        if (bindingResult.hasErrors()) {
            return "user/update_contact";
        }

        logger.info("Contact updated processing : ");

        var contact = contactService.getContactById(contactId);

        contact.setId(contactId);
        contact.setName(contactForm.getName());
        contact.setEmail(contactForm.getEmail());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setAddress(contactForm.getAddress());
        contact.setDescription(contactForm.getDescription());
        contact.setFavorite(contactForm.isFavorite());
        contact.setWebsiteLink(contactForm.getWebsiteLink());

        if (contactForm.getContactImage() != null && !contactForm.getContactImage().isEmpty()) {
            logger.info("file is not empty");
            String fileName = UUID.randomUUID().toString();
            String imageUrl = imageService.uploadImage(contactForm.getContactImage(), fileName);
            contact.setCloudinaryImagePublicId(fileName);
            contact.setContactImage(imageUrl);
            contactForm.setPicture(imageUrl);

        } else {
            logger.info("file is empty");
        }

        var updatedContact = contactService.updateContact(contact);

        logger.info("Contact updated successfully : " + updatedContact);

        session.setAttribute("msg",
                Message.builder()
                        .message("Contact updated successfully")
                        .messageType(MessageType.green)
                        .build());
        return "redirect:/user/contacts/view/" + contactId;

    }

}
