package com.moocafe.project.controller.headOffice;

import com.moocafe.project.service.FranchiseService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/headOffice")
@RequiredArgsConstructor
public class HeadEtcController {
    private final FranchiseService franchiseService;
    @GetMapping("/franchiseList")
    public String franchiseList(Model model) {

        return "headOffice/franchiseList";
    }
    @GetMapping("/memberInsert")
    public String memberInsert(Model model) {

        return "headOffice/memberInsert";
    }
}
