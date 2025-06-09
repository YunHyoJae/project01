package com.moocafe.project.controller.storeOwner;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/return")
public class ReturnController {
    @GetMapping("/return")
    public String returnView() {
        return "return/return";
    }

    @PostMapping("/return")
    public String returnPost() {
        return "return/return";
    }
}
