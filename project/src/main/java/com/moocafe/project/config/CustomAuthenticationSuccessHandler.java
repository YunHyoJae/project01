package com.moocafe.project.config;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String targetUrl = getSavedRequestRedirectUrl(request);
        if (targetUrl == null) {
            targetUrl = determineTargetUrl(authentication);
        }

        if (response.isCommitted()) {
            return;
        }
        redirectStrategy.sendRedirect(request, response, targetUrl);
    }

    private String getSavedRequestRedirectUrl(HttpServletRequest request) {
        SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, null);
        return (savedRequest != null) ? savedRequest.getRedirectUrl() : null;
    }

    protected String determineTargetUrl(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN")) || authorities.contains(new SimpleGrantedAuthority("ROLE_MANAGER"))) {
            return "/headOffice/index";
        } else if (authorities.contains(new SimpleGrantedAuthority("ROLE_USER"))) {
            return "/storeOwner/index";
        } else {
            return "/";
        }
    }
}