package com.scm.smartContactManager.config;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.scm.smartContactManager.constants.AppConstants;
import com.scm.smartContactManager.constants.AuthProvider;
import com.scm.smartContactManager.models.UserModel;
import com.scm.smartContactManager.repositories.UserRepo;
import com.scm.smartContactManager.services.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OA2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private Logger logger = LoggerFactory.getLogger(OA2AuthenticationSuccessHandler.class);

    @Autowired
    private UserRepo repo;

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        logger.info("OA2AuthenticationSuccessHandler");

        DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();

        logger.info("User logged using the below method");

        // TODO: Their might be some error will occure when user try to login
        // So, I'm assuming that user's email is already verified.
        // - then we need to change the default password

        /**
         * 1. We will put password block empty and we user logged in then give a pop to
         * fill password.
         * 2. We will set a random password and we give a pop to reset the password
         * 3. If the user logged with the same account with google and github then we also need to check the email is already exist
         * 
         * 
         */

        logger.info(user.getName());
        user.getAttributes().forEach((key, value) -> logger.info("{}: {}", key,
                value));

        // logger.info(user.getAuthorities().toString());

        var oauth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;

        String authorizedClientRegistrationId = oauth2AuthenticationToken.getAuthorizedClientRegistrationId();

        UserModel newUser = new UserModel();

        newUser.setEmailVerified(true);
        newUser.setActive(true);

        newUser.setPassword("password");

        if (authorizedClientRegistrationId.equalsIgnoreCase("google")) {

            newUser.setName(user.getAttribute("name"));
            newUser.setEmail(user.getAttribute("email").toString());
            newUser.setAvatar(user.getAttribute("picture").toString());
            newUser.setProvider(AuthProvider.GOOGLE);
            newUser.setProviderId(user.getName());

        } else if (authorizedClientRegistrationId.equalsIgnoreCase("github")) {

            newUser.setEmail(
                    user.getAttribute("email") != null ? user.getAttribute("email")
                            : user.getAttribute("login").toString() + "@gmail.com");

            newUser.setAvatar(user.getAttribute("avatar_url").toString());
            newUser.setName(user.getAttribute("login").toString());
            newUser.setProvider(AuthProvider.GitHub);
            newUser.setProviderId(user.getAttribute(user.getName()));

        }

        logger.info(user.getAuthorities().toString());

        logger.info(oauth2AuthenticationToken.getAuthorizedClientRegistrationId());

        // String email = user.getAttribute("email");
        // String name = user.getAttribute("name");
        // String avatar = user.getAttribute("picture").toString();

        // System.out.println("Email: " + email + " Name: " + name + " Avatar: " +
        // avatar);

        // UserModel newUser = new UserModel();

        // newUser.setName(name);
        // newUser.setEmail(email);
        // newUser.setAvatar(avatar);
        // newUser.setPassword("password");

        // newUser.setProvider(authorizedClientRegistrationId == "google" ?
        // AuthProvider.GOOGLE : AuthProvider.GitHub);
        // newUser.setProviderId(user.getName());

        // newUser.setAbout("");
        // newUser.setAddress("");

        UserModel availableUser = repo.findByEmail(newUser.getEmail());

        if (availableUser == null) {

            userService.saveUser(newUser);
            logger.info("User saved successfully with email " + newUser.getEmail());

        } else {
            userService.updateUser(newUser);
            logger.info("User updated successfully with email " + newUser.getEmail());
        }

        new DefaultRedirectStrategy().sendRedirect(request, response, "/user/dashboard");
    }

}
