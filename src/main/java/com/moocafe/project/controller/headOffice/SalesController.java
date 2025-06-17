package com.moocafe.project.controller.headOffice;

import com.moocafe.project.dto.SalesSummaryDto;
import com.moocafe.project.entity.Menu;
import com.moocafe.project.entity.MenuPrice;
import com.moocafe.project.entity.Sales;
import com.moocafe.project.repository.*;
import com.moocafe.project.service.InventoryStoreService;
import com.moocafe.project.service.MenuService;
import com.moocafe.project.service.SalesService;
import com.moocafe.project.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.io.UnsupportedEncodingException;
import java.util.Objects;

@Controller
public class SalesController {

    private final StoreService storeService;
    private final SalesService salesService;
    private final MenuService menuService;
    private final InventoryStoreService inventoryStoreService;

    public SalesController(
            SalesService salesService,
            MenuService menuService,
            InventoryStoreService inventoryStoreService,
            StoreService storeService
    ) {
        this.salesService = salesService;
        this.menuService = menuService;
        this.inventoryStoreService = inventoryStoreService;
        this.storeService = storeService;
    }

    @GetMapping("/headOffice/salesList")
    public String showSalesList(
            @RequestParam(required = false) Integer storeId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestParam(required = false) String menuId,  // ✅ 이 줄 추가
            Model model
    ) {
        if (endDate != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(endDate);
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            cal.set(Calendar.MILLISECOND, 999);
            endDate = cal.getTime();
        }

        List<Sales> salesList;

        if (storeId != null || (startDate != null && endDate != null)) {
            salesList = salesService.findByStoreIdAndSaleTimeBetween(
                    storeId,
                    startDate,
                    endDate
            );
        } else {
            salesList = salesService.findAll();
        }

        // SalesSummaryDto 리스트에서 합계 구하기
        List<SalesSummaryDto> summaryList = salesService.findSalesSummaryTotal(storeId, startDate, endDate);

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
        model.addAttribute("salesList", salesList);
        model.addAttribute("storeId", storeId);
        model.addAttribute("storeList", storeService.findAll());
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("menuList", menuService.getDistinctMenuListForSaleInput());
        model.addAttribute("totalQuantity", totalQuantity);
        model.addAttribute("totalAmount", totalAmount);

        return "headOffice/salesList";
    }

    @PostMapping("/headOffice/salesInput")
    @Transactional
    public String inputSale(
            @RequestParam Integer storeId,
            @RequestParam String menuId,
            @RequestParam Integer quantity,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date saleTime,
            Model model
    ) {
        try {
            List<Menu> menuItems = menuService.findByMenuIdWithPrice(menuId);
            if (menuItems.isEmpty()) {
                throw new IllegalArgumentException("해당 메뉴 ID에 가격이 등록되지 않았습니다.");
            }

            for (Menu item : menuItems) {
                int totalUsed = item.getQuantityUsed() * quantity;
                Integer currentStockObj = inventoryStoreService.findQuantityByStoreIdAndItemCode(storeId, item.getItemCode());
                int currentStock = currentStockObj != null ? currentStockObj : 0;

                if (currentStock < totalUsed) {
                    String msg = "[" + item.getItemCode() + "] 품목 재고가 부족합니다. (필요: " + totalUsed + " / 보유: " + currentStock + ")";
                    try {
                        return "redirect:/headOffice/salesList?alert=" + java.net.URLEncoder.encode(msg, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException("인코딩 실패", e);
                    }
                }

                inventoryStoreService.decreaseStock(storeId, item.getItemCode(), totalUsed);
            }

            MenuPrice price = menuService.findByMenuId(menuId)
                    .orElseThrow(() -> new IllegalArgumentException("가격 정보 없음"));

            String menuName = menuItems.get(0).getMenuName();
            Sales sale = new Sales(storeId, menuId, menuName, quantity);

            if (saleTime != null) {
                Field field = Sales.class.getDeclaredField("saleTime");
                field.setAccessible(true);
                field.set(sale, saleTime);
            }

            model.addAttribute("menuList", menuService.getDistinctMenuListForSaleInput());

            salesService.save(sale);
            return "redirect:/headOffice/salesList";

        } catch (Exception e) {
            String msg = "판매 등록 중 오류가 발생했습니다: " + e.getMessage();
            try {
                return "redirect:/headOffice/salesList?alert=" + java.net.URLEncoder.encode(msg, "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                throw new RuntimeException("인코딩 실패", ex);
            }
        }
    }


}