package com.taskmanagement.taskmanagement.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String redirectUrl = "/";

        if (!authentication.getAuthorities().isEmpty()) {
            GrantedAuthority grantedAuthority = authentication.getAuthorities().iterator().next();

            if (grantedAuthority.getAuthority().equals("ROLE_ADMIN")) {
                redirectUrl = "/admin-panel";
            } else if (grantedAuthority.getAuthority().equals("ROLE_USER")) {
                redirectUrl = "/tasks";
            }
        }

        response.sendRedirect(redirectUrl);
    }

}
