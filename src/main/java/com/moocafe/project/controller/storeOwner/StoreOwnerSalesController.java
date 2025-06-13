package com.moocafe.project.controller.storeOwner;

import com.moocafe.project.dto.CustomUserDetails;
import com.moocafe.project.dto.SaleRegisterDto;
import com.moocafe.project.dto.SalesSummaryDto;
import com.moocafe.project.entity.*;
import com.moocafe.project.repository.*;
import com.moocafe.project.service.MenuService;
import com.moocafe.project.service.SalesService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class StoreOwnerSalesController {

    private final SalesService salesService;
    private final MemberStoreRepository memberStoreRepository;
    private final MenuService menuService;
    private final MenuRepository menuRepository;
    private final InventoryStoreRepository inventoryStoreRepository;

    @GetMapping("/storeOwner/salesList")
    public String viewSalesList(@AuthenticationPrincipal CustomUserDetails userDetails,
                                @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                                @RequestParam(required = false) String menuName,
                                Model model) {

        Member loginMember = userDetails.getLoggedMember();
        List<MemberStore> msList = memberStoreRepository.findByMemberId(loginMember.getId());

        if (!msList.isEmpty()) {
            Store store = msList.get(0).getStore(); // 첫 번째 매장 기준
            Integer storeId = store.getId();

            if (startDate == null) {
                startDate = LocalDate.now().withDayOfMonth(1);
            }
            if (endDate == null) {
                endDate = LocalDate.now();
            }

            // SalesSummaryDto 리스트에서 합계 구하기
            List<SalesSummaryDto> summaryList = salesService.getSalesSummary(storeId, startDate, endDate, menuName);

            // 총 수량 및 총 매출액 구하기
            int totalQuantity = summaryList.stream()
                    .map(SalesSummaryDto::getTotalQuantity)
                    .mapToInt(qty -> qty != null ? qty.intValue() : 0)
                    .sum();

            BigDecimal totalAmount = summaryList.stream()
                    .map(SalesSummaryDto::getTotalAmount)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            model.addAttribute("summaryList", summaryList);
            model.addAttribute("storeId", storeId);
            model.addAttribute("storeList", List.of(store));
            model.addAttribute("menuList", menuService.getMenuListForStoreOwner());
            model.addAttribute("startDate", java.sql.Date.valueOf(startDate));
            model.addAttribute("endDate", java.sql.Date.valueOf(endDate));
            model.addAttribute("totalQuantity", totalQuantity);
            model.addAttribute("totalAmount", totalAmount);
            model.addAttribute("menuName", menuName);
        }

        return "storeOwner/salesList"; // 📄 점주용 템플릿 (복사 필요)
    }

    @PostMapping("/storeOwner/salesInput")
    public String registerSale(@ModelAttribute SaleRegisterDto dto,
                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        Member loginMember = userDetails.getLoggedMember();
        List<MemberStore> msList = memberStoreRepository.findByMemberId(loginMember.getId());

        if (msList.isEmpty()) {
            return "redirect:/storeOwner/salesList";
        }

        Store store = msList.get(0).getStore();
        dto.setStoreId(store.getId());

        List<Menu> menuItems = menuRepository.findByMenuIdWithPrice(dto.getMenuId());
        if (menuItems.isEmpty()) {
            return redirectWithAlert("해당 메뉴 ID에 해당하는 구성 재료가 없습니다.");
        }

        for (Menu item : menuItems) {
            int totalUsed = item.getQuantityUsed() * dto.getQuantity();
            int currentStock = inventoryStoreRepository.findQuantityByStoreIdAndItemCode(dto.getStoreId(), item.getItemCode());

            if (currentStock < totalUsed) {
                String msg = "[" + item.getItemCode() + "] 재고 부족 (필요: " + totalUsed + " / 보유: " + currentStock + ")";
                return redirectWithAlert(msg);
            }

            inventoryStoreRepository.decreaseStock(dto.getStoreId(), item.getItemCode(), totalUsed);
        }

        // 메뉴명 주입 및 saleTime 처리
        dto.setMenuName(menuService.getMenuNameById(dto.getMenuId()));
        Sales sale = new Sales(dto.getStoreId(), dto.getMenuId(), dto.getMenuName(), dto.getQuantity());

        if (dto.getSaleTime() != null) {
            try {
                Field field = Sales.class.getDeclaredField("saleTime");
                field.setAccessible(true);
                field.set(sale, java.sql.Timestamp.valueOf(dto.getSaleTime()));
            } catch (Exception e) {
                return redirectWithAlert("판매 시간 설정 실패: " + e.getMessage());
            }
        }

        salesService.saveSaleEntity(sale);
        return "redirect:/storeOwner/salesList";
    }

    private String redirectWithAlert(String msg) {
        try {
            return "redirect:/storeOwner/salesList?alert=" + java.net.URLEncoder.encode(msg, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException("인코딩 오류", e);
        }
    }


}