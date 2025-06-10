package com.moocafe.project.controller.storeOwner;

import com.moocafe.project.dto.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/storeOwner")
public class StoreEtcController {
    @GetMapping("/etcOwnerFaqList")
    public String etcOwnerFaqList() {
        return "storeOwner/etcOwnerFaqList";
    }

}
