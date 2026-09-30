package com.hirehub.util;

import com.hirehub.contants.ApplicationConstants;
import com.hirehub.entity.HireHubUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class ApplicationUtility {

    public static String getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
                authentication.getPrincipal().equals("anonymousUser")) {
            return ApplicationConstants.SYSTEM;
        }
        Object principal = authentication.getPrincipal();
        String username;
        if (principal instanceof HireHubUser hireHubUser) {
            username = hireHubUser.getEmail();
        } else {
            username = principal.toString(); // fallback
        }
        return username;
    }
}
