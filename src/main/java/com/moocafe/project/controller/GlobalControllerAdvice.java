package com.moocafe.project.controller;

import com.moocafe.project.dto.CustomUserDetails;
import com.moocafe.project.dto.LoginDto;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {
    @ModelAttribute("LoginDto")
    public LoginDto addUserToModel(@AuthenticationPrincipal CustomUserDetails user) {
        return user != null ? user.toDto() : null;
    }
}