package com.moocafe.project.controller;

import com.moocafe.project.dto.FranchiseBoardSaveDto;
import com.moocafe.project.service.FranchiseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class BrandController {
    private final FranchiseService fs;
    @GetMapping({"/","/index"})
    public String index() {
        return "index/index";
    }
    @GetMapping("/brand/franchise")
    public String franchise(@RequestParam(required=false) Integer success, Model model) {
        if (success != null) {
        System.out.println("success = " + success);
        if (success == 0) {
            model.addAttribute("successMessage", "저장에 실패했습니다. 다시 시도해주세요.");
        }else if(success == 1) {
            model.addAttribute("successMessage", "문의하신 내역이 전달 되었습니다. 상담원이 확인 후 곧 연락드리겠습니다.");
        }
        }
        model.addAttribute("dto", new FranchiseBoardSaveDto());
        return "brand/franchise";
    }
    @PostMapping("/brand/franchise")
    public String franchise(@Valid @ModelAttribute FranchiseBoardSaveDto dto, BindingResult bindingResult) {
        System.out.println("dto = " + dto);
        if (bindingResult.hasErrors()) {
            return "brand/franchise";}
        int result = fs.save(dto);
        if (result == 1) {return "redirect:/brand/franchise?success=1";}else{
            return "redirect:/brand/franchise?success=0";
        }
    }
    @GetMapping("/login")
    public String login(){
        return "index/login";
    }
}
