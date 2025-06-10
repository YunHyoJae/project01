package com.moocafe.project.controller.headOffice;

import com.moocafe.project.dto.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/headOffice")
public class HeadIndexController {
    @GetMapping("/index")
    public String index(Model model, @AuthenticationPrincipal CustomUserDetails user) {
        model.addAttribute("user", user.toDto());
        return "headOffice/index";
    }
}
