package com.scm.smartContactManager.helper;

import java.security.Principal;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class UserHelper {

    public static String getEmailOfLoggedInUser(Authentication authentication) {

        if (authentication instanceof OAuth2AuthenticationToken) {

            var oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;

            var clientId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();

            var oAuth2User = (OAuth2User) authentication.getPrincipal();

            if (clientId.equalsIgnoreCase("google")) {

                // when logged in with google account

                System.out.println("Logged in with google account");

                return oAuth2User.getAttribute("email").toString();

            } else if (clientId.equalsIgnoreCase("github")) {

                // when logged in with github account
                System.out.println("Logged in with github account");

                return oAuth2User.getAttribute("email") != null ? oAuth2User.getAttribute("email")
                        : oAuth2User.getAttribute("login").toString() + "@gmail.com";
            }

        } else {

            // when logged with normal email , password

            return authentication.getName();

        }

        return "";
    }

}
