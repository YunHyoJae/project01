package com.moocafe.project.controller.headOffice;

import com.moocafe.project.dto.*;
import com.moocafe.project.entity.Member;
import com.moocafe.project.service.FaqService;
import com.moocafe.project.service.FranchiseService;
import com.moocafe.project.service.MemberStoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/headOffice")
@RequiredArgsConstructor
public class HeadEtcController {
    private final FranchiseService franchiseService;
    private final FaqService faqService;
    private final MemberStoreService memberStoreService;
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
    public String faqList(@RequestParam(defaultValue = "") String category, @RequestParam(defaultValue = "") String keyword, @PageableDefault(size = 10,sort="regDate", direction = Sort.Direction.DESC) Pageable pageable,Model model) {
        Page<FaqBoardDto> fb =faqService.page(category, keyword, pageable);
        model.addAttribute("list",fb.getContent());
        PageDto<FaqBoardDto> pageDto=new PageDto<>(fb,category,keyword,"/storeOwner/faqList");
        model.addAttribute("pageDto",pageDto);
        return "headOffice/faqList";
    }
    @GetMapping("/faqDetail/{id}")
    public String faqDetail(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("content",faqService.get(id));
        return "headOffice/faqDetail";
    }
    @PostMapping("/faqDetail")
    public String faqDetail(@ModelAttribute FaqReplySaveDto dto, @AuthenticationPrincipal CustomUserDetails user) {
        dto.setMember(user.getLoggedMember());
        int response = faqService.saveReply(dto);
        if(response==1){
            return "redirect:/headOffice/faqList";
        }else{
            return "headOffice/faqDetail";
        }
    }
    @GetMapping("/memberList")
    public String memberList(@RequestParam(defaultValue = "") String category, @RequestParam(defaultValue = "") String keyword, @PageableDefault(size = 10,sort="regDate", direction = Sort.Direction.DESC) Pageable pageable,Model model) {
        //model.addAttribute("list",memberStoreService.getAll());

        Page<MemberStoreDto> fb =memberStoreService.getAllPage(category, keyword, pageable);
        model.addAttribute("list",fb.getContent());
        PageDto<MemberStoreDto> pageDto=new PageDto<>(fb,category,keyword,"/headOffice/memberList");
        model.addAttribute("pageDto",pageDto);

        return "headOffice/memberList";
    }
    @GetMapping("/memberDetail/{id}")
    public String memberDetail(@PathVariable("id") Integer id, Model model) {
        MemberStoreDto msd = memberStoreService.getStoreMember(id);
        model.addAttribute("dto", msd != null ? msd : new MemberStoreDto() );
        return "headOffice/memberDetail";
    }
    @PostMapping("/memberDetail")
    public String memberDetail(@Valid @ModelAttribute("dto") MemberStoreDto dto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("dto", dto);
            return "headOffice/memberDetail";
        }
        if(dto.getStateBoolean()){dto.setState("휴업");}else{dto.setState("영업중");};
        memberStoreService.update(dto);
        return "redirect:/headOffice/memberList";
    }
    @GetMapping("/memberAdd")
    public String memberAdd(Model model){
        model.addAttribute("dto",new MemberStoreDto());
        return "headOffice/memberAdd";
    }
    @PostMapping("/memberAdd")
    public String memberAdd(@Valid @ModelAttribute("dto") MemberStoreDto dto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("dto", dto);
            return "headOffice/memberAdd";
        }
        if(dto.getStateBoolean()){dto.setState("영업중");}else{dto.setState("오픈예정");};
        memberStoreService.save(dto);
        return "redirect:/headOffice/memberList";
    }
}
