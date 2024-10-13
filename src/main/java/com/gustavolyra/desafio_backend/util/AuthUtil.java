package com.gustavolyra.desafio_backend.util;

import com.gustavolyra.desafio_backend.models.entities.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtil {

    private AuthUtil() {
    }

    public static User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        var principal = auth.getPrincipal();
        return (User) principal;
    }


}
