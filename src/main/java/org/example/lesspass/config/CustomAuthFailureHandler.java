// language: java
package org.example.lesspass.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;

import java.io.IOException;

@Component
public class CustomAuthFailureHandler implements AuthenticationFailureHandler {

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        org.springframework.security.core.AuthenticationException exception)
            throws IOException, ServletException {

        String targetUrl = "/login?error=default";
        if (exception instanceof UsernameNotFoundException) {
            targetUrl = "/login?error=usernotfound";
        } else if (exception instanceof BadCredentialsException) {
            targetUrl = "/login?error=badcredentials";
        } else {
            targetUrl = "/login?error=default";
        }

        redirectStrategy.sendRedirect(request, response, targetUrl);
    }
}
