package com.scm.smartContactManager.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.smartContactManager.models.UserModel;
import com.scm.smartContactManager.services.UserService;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/verify-email")
    public String verificationOfEmail(@RequestParam("t") String emailToken) {

        System.out.println("Verifying email :" + emailToken);

        UserModel user = userService.verifyEmailByToken(emailToken);

        if (user != null) {

            if (user.getEmailVerifyToken().equals(emailToken)) {

                user.setEmailVerified(true);
                user.setActive(true);
                user.setEmailVerifyToken(null);

                userService.updateUser(user);

                return "success_page";
            }

        }

        return "error_page";
    }

}
