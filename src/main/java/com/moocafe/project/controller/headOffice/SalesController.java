package com.moocafe.project.controller.headOffice;

import com.moocafe.project.dto.SalesSummaryDto;
import com.moocafe.project.entity.Menu;
import com.moocafe.project.entity.MenuPrice;
import com.moocafe.project.entity.Sales;
import com.moocafe.project.entity.Store;
import com.moocafe.project.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.io.UnsupportedEncodingException;

@Controller
public class SalesController {

    private final StoreRepository storeRepository;
    private final SalesRepository salesRepository;
    private final MenuRepository menuRepository;
    private final InventoryStoreRepository inventoryStoreRepository;
    private final MenuPriceRepository menuPriceRepository;

    @Autowired
    public SalesController(SalesRepository salesRepository,
                           MenuRepository menuRepository,
                           InventoryStoreRepository inventoryStoreRepository,
                           MenuPriceRepository menuPriceRepository,
                           StoreRepository storeRepository) {
        this.salesRepository = salesRepository;
        this.menuRepository = menuRepository;
        this.inventoryStoreRepository = inventoryStoreRepository;
        this.menuPriceRepository = menuPriceRepository;
        this.storeRepository = storeRepository;
    }

    @GetMapping("/headOffice/salesList")
    public String showSalesList(
            @RequestParam(required = false) Integer storeId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestParam(required = false) String menuId,  // ✅ 이 줄 추가
            Model model
    ) {
        List<Sales> salesList;

        if (storeId != null || (startDate != null && endDate != null)) {
            salesList = salesRepository.findByStoreIdAndSaleTimeBetween(
                    storeId,
                    startDate,
                    endDate
            );
        } else {
            salesList = salesRepository.findAll();
        }

        List<Store> storeList = storeRepository.findAll();
        List<SalesSummaryDto> summaryList = salesRepository.findSalesSummary(storeId, startDate, endDate);
        model.addAttribute("summaryList", summaryList);
        model.addAttribute("salesList", salesList);
        model.addAttribute("storeId", storeId);
        model.addAttribute("storeList", storeRepository.findAll());
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("menuList", menuRepository.findMenusWithPrice());
//        model.addAttribute("menuList", menuList); // 뷰에서 사용 가능
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
            List<Menu> menuItems = menuRepository.findByMenuIdWithPrice(menuId);
            if (menuItems.isEmpty()) {
                throw new IllegalArgumentException("해당 메뉴 ID에 가격이 등록되지 않았습니다.");
            }

            for (Menu item : menuItems) {
                int totalUsed = item.getQuantityUsed() * quantity;
                int currentStock = inventoryStoreRepository.findQuantityByStoreIdAndItemCode(storeId, item.getItemCode());

                if (currentStock < totalUsed) {
                    String msg = "[" + item.getItemCode() + "] 품목 재고가 부족합니다. (필요: " + totalUsed + " / 보유: " + currentStock + ")";
                    try {
                        return "redirect:/headOffice/salesList?alert=" + java.net.URLEncoder.encode(msg, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException("인코딩 실패", e);
                    }
                }

                inventoryStoreRepository.decreaseStock(storeId, item.getItemCode(), totalUsed);
            }

            MenuPrice price = menuPriceRepository.findByMenuId(menuId)
                    .orElseThrow(() -> new IllegalArgumentException("가격 정보 없음"));

            String menuName = menuItems.get(0).getMenuName();
            Sales sale = new Sales(storeId, menuId, menuName, quantity);

            if (saleTime != null) {
                Field field = Sales.class.getDeclaredField("saleTime");
                field.setAccessible(true);
                field.set(sale, saleTime);
            }

            salesRepository.save(sale);
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