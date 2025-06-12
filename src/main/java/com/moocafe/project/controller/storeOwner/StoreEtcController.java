package com.moocafe.project.controller.storeOwner;

import com.moocafe.project.dto.*;
import com.moocafe.project.service.FaqService;
import com.moocafe.project.service.MemberStoreService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/storeOwner")
@RequiredArgsConstructor
public class StoreEtcController {
//    public final FaqService fs;
//    public final MemberStoreService mss;
//    @GetMapping({"/faqList", "/faqList/owner"})
//    public String etcFaqList(@RequestParam(defaultValue = "") String category,
//                             @RequestParam(defaultValue = "") String keyword,
//                             @PageableDefault(size = 10,sort="regDate", direction = Sort.Direction.DESC) Pageable pageable,
//                             Model model,
//                             @AuthenticationPrincipal CustomUserDetails user,
//                             HttpServletRequest request) {
//        String uri = request.getRequestURI();
//        boolean isOwner = uri.contains("/owner");
//        Page<FaqBoardDto> fb;
//        PageDto<FaqBoardDto> pageDto;
//        if (isOwner) {
//            fb=fs.memberPage(category, keyword, user.getLoggedMember(),pageable);
//            model.addAttribute("list", fb.getContent());
//            pageDto=new PageDto<>(fb,category,keyword,"/storeOwner/faqList/owner");
//            model.addAttribute("pageDto",pageDto);
//            model.addAttribute("ownerState","owner");
//        } else {
//            fb=fs.page(category, keyword, pageable);
//            model.addAttribute("list", fb.getContent());
//            pageDto=new PageDto<>(fb,category,keyword,"/storeOwner/faqList");
//            model.addAttribute("pageDto",pageDto);
//            model.addAttribute("ownerState","list");
//        }
//        return "storeOwner/etcFaqList";
//    }
//    @GetMapping({"/faqDetail/{id}","/faqDetail/owner/{id}"})
//    public String etcFaqDetail(@PathVariable("id") Integer id, Model model,HttpServletRequest request) {
//        model.addAttribute("content",fs.get(id));
//        String uri = request.getRequestURI();
//        boolean isOwner = uri.contains("/owner");
//        if (isOwner) {
//            model.addAttribute("ownerState","owner");
//        }else{
//            model.addAttribute("ownerState","list");
//        }
//        return "storeOwner/etcFaqDetail";
//    }
//    @GetMapping("/faqEdit")
//    public String etcFaqEdit(Model model) {
//        model.addAttribute("dto", new FaqBoardSaveDto());
//        return "storeOwner/etcFaqEdit";
//    }
//    @PostMapping("/faqEdit")
//    public String etcFaqEdit(@Valid @ModelAttribute("dto") FaqBoardSaveDto dto, BindingResult bindingResult, Model model, @AuthenticationPrincipal CustomUserDetails user) {
//        if (bindingResult.hasErrors()) {
//            model.addAttribute("dto", dto);
//            return "storeOwner/etcFaqEdit";
//        }
//        dto.setMember(user.getLoggedMember());
//        int result = fs.save(dto);
//        if(result == 1) {return "redirect:/storeOwner/faqList";}
//        else{return "redirect:/storeOwner/faqEdit";}
//    }
//    @GetMapping("/member")
//    public String etcMember(Model model,@AuthenticationPrincipal CustomUserDetails user) {
//        model.addAttribute("dto", mss.getMemberStore(user.getLoggedMember()));
//        return "storeOwner/etcMember";
//    }
//    @PostMapping("/member")
//    public String etcMember(@Valid @ModelAttribute("dto") MemberStoreDto dto, BindingResult bindingResult, Model model, @AuthenticationPrincipal CustomUserDetails user) {
//        if (bindingResult.hasErrors()) {
//            model.addAttribute("dto", dto);
//            return "storeOwner/etcMember";
//        }
//        if (Objects.equals(dto.getUserId(), user.getUsername())){
//            int result=mss.update(dto);
//        }
//        return "redirect:/storeOwner/member";
//    }

}
