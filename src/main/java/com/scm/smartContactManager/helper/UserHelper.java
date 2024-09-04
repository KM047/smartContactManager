package com.scm.smartContactManager.helper;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.scm.smartContactManager.models.UserModel;

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

    public static String getLinkForEmailVerification(String emailToken, UserModel user) {

        String verifyLink = "http://localhost:8080/auth/verify-email?t=" + emailToken;

        String greeting = "Dear " + user.getName() + ",";
        String instructions = "Please click the following link to verify your email address: ";
        String verificationLink = "<a href=\"" + verifyLink + "\">Verify Email</a>";
        String supportInfo = "If you did not request this email, please ignore it. For any issues, contact our support team at support@example.com.";
        String companyLogo = "<img src='https://example.com/logo.png' alt='Company Logo'>";
        String footer = "Best regards,<br>Company Name<br><a href='https://example.com/privacy'>Privacy Policy</a> | <a href='https://example.com/terms'>Terms of Service</a>";

        String plainTextEmail = "Dear " + user.getName()
                + ",\n\nPlease click the following link to verify your email address: " + verifyLink
                + "\n\nIf you did not request this email, please ignore it. For any issues, contact our support team at support@example.com.\n\nBest regards,\nCompany Name";

        String emailContent = "<html><body>";
        emailContent += companyLogo;
        emailContent += "<p>" + greeting + "</p>";
        emailContent += "<p>" + instructions + "</p>";
        emailContent += "<p>" + verificationLink + "</p>";
        emailContent += "<p>" + supportInfo + "</p>";
        emailContent += "<p>" + footer + "</p>";
        emailContent += "</body></html>";

        return instructions + verifyLink;

    }

}
