package com.moocafe.project.controller.headOffice;

import com.moocafe.project.dto.CustomUserDetails;
import com.moocafe.project.dto.FranchiseBoardDto;
import com.moocafe.project.dto.FranchiseReplySaveDto;
import com.moocafe.project.dto.PageDto;
import com.moocafe.project.entity.Member;
import com.moocafe.project.service.FranchiseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/headOffice")
@RequiredArgsConstructor
public class HeadEtcController {
    private final FranchiseService franchiseService;
    @GetMapping("/franchiseList")
    public String franchiseList(@RequestParam(defaultValue = "") String category, @RequestParam(defaultValue = "") String keyword, @PageableDefault(size = 10,sort="regDate", direction = Sort.Direction.DESC)Pageable pageable, Model model, Sort sort) {
        Page<FranchiseBoardDto> fb=franchiseService.page(category, keyword, pageable);
        model.addAttribute("list",fb.getContent());
        PageDto<FranchiseBoardDto> pageDto=new PageDto<>(fb,category,keyword,"/headOffice/franchiseList");
        model.addAttribute("pageDto",pageDto);
        return "headOffice/franchiseList";
    }
    @GetMapping("/franchiseDetail/{id}")
    public String franchiseDetail(@PathVariable("id") int id, Model model, @AuthenticationPrincipal CustomUserDetails user) {
        model.addAttribute("content",franchiseService.update(id,user.getLoggedMember(),"상담중"));
        return "headOffice/franchiseDetail";
    }
    @PostMapping("/franchiseDetail")
    public String franchiseDetail(@ModelAttribute FranchiseReplySaveDto dto, @AuthenticationPrincipal CustomUserDetails user) {
        dto.setMember(user.getLoggedMember());
        int response = franchiseService.update2(dto,"상담완료");
        if(response==1){
            return "redirect:/headOffice/franchiseList";
        }else{
            return "headOffice/franchiseDetail";
        }
    }
    @GetMapping("/faqList")
    public String faqList(Model model) {

        return "headOffice/faqList";
    }
    @GetMapping("/faqDetail/{id}")
    public String faqDetail(@PathVariable("id") int id, Model model) {

        return "headOffice/faqList";
    }
    @GetMapping("/memberList")
    public String memberList(Model model) {

        return "headOffice/memberInsert";
    }
    @GetMapping("/memberInsert")
    public String memberInsert(Model model) {

        return "headOffice/memberInsert";
    }
}
