package com.moocafe.project.controller.storeOwner;

import com.moocafe.project.dto.CustomUserDetails;
import com.moocafe.project.entity.ReturnItem;
import com.moocafe.project.repository.ReturnItemRepository;
import com.moocafe.project.service.ReturnService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("storeOwner")
@RequiredArgsConstructor
@Slf4j
public class StoreReturnController {

    private final ReturnService returnService;

    @GetMapping("/returnList")
    public String returnList(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        int storeId = userDetails.getStoreId(); // 로그인한 유저의 매장 ID
        log.info("storeId = " + storeId);

        List<ReturnItem> returnItems = returnService.findByStoreId(storeId);
        log.info("returnItems: " + returnItems);

        model.addAttribute("returnList", returnItems);
        return "storeOwner/returnList"; // HTML 파일 경로
    }
}
