package com.moocafe.project.controller.storeOwner;

import com.moocafe.project.dto.CustomUserDetails;
import com.moocafe.project.dto.FaqBoardDto;
import com.moocafe.project.dto.FranchiseBoardDto;
import com.moocafe.project.dto.PageDto;
import com.moocafe.project.service.FaqService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/storeOwner")
@RequiredArgsConstructor
public class StoreEtcController {
    public final FaqService fs;
    @GetMapping({"/faqList", "/faqList/owner"})
    public String etcFaqList(@RequestParam(defaultValue = "") String category,
                             @RequestParam(defaultValue = "") String keyword,
                             @PageableDefault(size = 10,sort="regDate", direction = Sort.Direction.DESC) Pageable pageable,
                             Model model,
                             @AuthenticationPrincipal CustomUserDetails user,
                             HttpServletRequest request) {
        String uri = request.getRequestURI();
        boolean isOwner = uri.contains("/owner");
        Page<FaqBoardDto> fb;
        PageDto<FaqBoardDto> pageDto;
        if (isOwner) {
            fb=fs.memberPage(category, keyword, user.getLoggedMember(),pageable);
            model.addAttribute("list", fb.getContent());
            pageDto=new PageDto<>(fb,category,keyword,"/storeOwner/faqList/owner");
            model.addAttribute("pageDto",pageDto);
        } else {
            fb=fs.page(category, keyword, pageable);
            model.addAttribute("list", fb.getContent());
            pageDto=new PageDto<>(fb,category,keyword,"/storeOwner/faqList");
            model.addAttribute("pageDto",pageDto);
        }
        return "storeOwner/etcFaqList";
    }
    @GetMapping("/faqDetail")
    public String etcFaqDetail() {
        return "storeOwner/etcFaqDetail";
    }
    @GetMapping("/faqEdit")
    public String etcFaqEdit() {
        return "storeOwner/etcFaqEdit";
    }
    @GetMapping("/member")
    public String etcMember() {
        return "storeOwner/etcMember";
    }

}
