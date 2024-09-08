package com.scm.smartContactManager.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.scm.smartContactManager.helper.UserHelper;
import com.scm.smartContactManager.models.UserModel;
import com.scm.smartContactManager.services.UserService;

@ControllerAdvice
public class RootController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @ModelAttribute
    public void addUserDataToAllUserPages(Model model, Authentication authentication) {

        if (authentication == null) {
            return;
        }

        logger.info("Adding user data to all user pages");

        String loggedInUserEmail = UserHelper.getEmailOfLoggedInUser(authentication);

        if (loggedInUserEmail == null) {
            model.addAttribute("loggedInUser", null);
        } else {

            UserModel user = userService.getUserByEmail(loggedInUserEmail);

            model.addAttribute("loggedInUser", user);
        }

    }

}
