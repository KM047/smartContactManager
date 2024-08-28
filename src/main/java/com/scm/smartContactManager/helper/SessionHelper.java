package com.scm.smartContactManager.helper;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpSession;

@Component
public class SessionHelper {

    public static void removeMessage() {
        try {

            System.out.println("Removing message from the session");
            HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                    .getRequest()
                    .getSession();

            session.removeAttribute("msg");
        } catch (Exception e) {
            System.out.println("Session not found ");
        }
    }

}
