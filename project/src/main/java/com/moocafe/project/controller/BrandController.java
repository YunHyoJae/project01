package com.moocafe.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BrandController {
    @GetMapping({"/","/index"})
    public String index() {
        return "index/index";
    }
    @GetMapping("/brand/franchise")
    public String franchise(){
        return "brand/franchise";
    }
    @GetMapping("/login")
    public String login(){
        return "index/login";
    }
}
