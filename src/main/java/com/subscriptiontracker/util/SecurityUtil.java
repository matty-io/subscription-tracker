package com.subscriptiontracker.util;

import com.subscriptiontracker.exception.ApiException;
import com.subscriptiontracker.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class SecurityUtil {
    public static User getAuthenticatedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User user) {
            return user;
        } else {
            log.error("User requested is not found in SecurityContextHolder");
            throw ApiException.builder().message("Authentication required").status(401).build();
        }
    }
}
