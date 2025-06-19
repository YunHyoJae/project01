package com.moocafe.project.controller.storeOwner;

import com.moocafe.project.dto.CustomUserDetails;
import com.moocafe.project.dto.ReturnDto;
import com.moocafe.project.dto.ReturnItemDto;
import com.moocafe.project.entity.Return;
import com.moocafe.project.entity.ReturnItem;
import com.moocafe.project.repository.ReturnItemRepository;
import com.moocafe.project.service.ReturnService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("storeOwner")
@RequiredArgsConstructor
@Slf4j
public class
StoreReturnController {

    private final ReturnService returnService;

//    어제한거
//    @GetMapping("/returnList")
//    public String returnList(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
//        int storeId = userDetails.getStoreId(); // 로그인한 유저의 매장 ID
//        log.info("storeId = " + storeId);
//
//        List<Return> returnItems = returnService.findByStoreId(storeId);
//        log.info("returnItems: " + returnItems);
//
//        model.addAttribute("returnList", returnItems);
//        return "storeOwner/returnList"; // HTML 파일 경로
//    }

//    @GetMapping("/returnList")
//    public String returnListPage(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
//        Integer storeId = userDetails.getStoreId();
//        List<ReturnDto> returnList = returnService.getReturnsByStoreId(storeId);
//        model.addAttribute("returnList", returnList);
//        return "storeOwner/returnList"; // ⬅ Thymeleaf 페이지를 보여주고 싶을 때
//    }

    //강동현 입력
    @GetMapping("/returnList")
    public String returnListPage(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Integer storeId = userDetails.getStoreId();
        System.out.println("✅ storeId: " + storeId); // 로그 → 콘솔

        List<ReturnDto> returnList = returnService.getReturnsByStoreId(storeId);
        System.out.println("✅ returnList size: " + returnList.size());

        for (ReturnDto r : returnList) {
            System.out.println("반품번호: " + r.getReturnNumber());
            System.out.println("  - 품목 수: " + r.getItems().size());
            for (ReturnItemDto item : r.getItems()) {
                System.out.println("    > 품목명: " + item.getItemName() + ", 수량: " + item.getReturnQuantity() + ", 상태: " + item.getStatus());
            }
        }

        model.addAttribute("returnList", returnList);
        model.addAttribute("storeId", storeId);
        return "storeOwner/returnList"; // ⬅ Thymeleaf 페이지를 보여주고 싶을 때
    }

//
//    @PostMapping("/returnComplete")
//    @ResponseBody
//    public ResponseEntity<String> updateReturnStatus(@RequestParam Integer id ) {
//        log.info("id: " + id);
//        returnService.markAsCompleted(id);
//        return ResponseEntity.ok("updated");
//    }

    //강동현 입력



    @PostMapping("/storeOwner/returnComplete")
    @ResponseBody
    public String completeReturn(@RequestBody Map<String, String> payload) {
        String returnNumber = payload.get("returnNumber");
        String itemCode = payload.get("itemCode");

        if (returnNumber == null || itemCode == null) {
            throw new IllegalArgumentException("반품번호 또는 품목코드가 누락되었습니다.");
        }

        log.info("반품 완료 처리 요청 - returnNumber: {}, itemCode: {}", returnNumber, itemCode);

        returnService.completeItem(returnNumber, itemCode);

        return "ok";
    }

}