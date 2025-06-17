package com.moocafe.project.controller.headOffice;

import com.moocafe.project.constent.Role;
import com.moocafe.project.dto.*;
import com.moocafe.project.entity.*;
import com.moocafe.project.repository.*;
import com.moocafe.project.service.InventoryStoreService;
import com.moocafe.project.service.InventorySummaryService;
import com.moocafe.project.service.MemberStoreService;
import com.moocafe.project.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/headOffice/inventoryStore")
@RequiredArgsConstructor
public class InventoryStoreController {

    private final InventoryStoreService inventoryStoreService;
    private final InventorySummaryService inventorySummaryService;
    private final MemberStoreService memberStoreService;
    private final StoreService storeService;

    @GetMapping("/{storeId}")
    public String viewStoreInventory(@PathVariable Integer storeId, Model model) {
        List<InventorySummaryPivotRowDto> inventory = inventorySummaryService.generateInventoryPivotByStoreId(storeId);
        model.addAttribute("inventoryList", inventory);
        model.addAttribute("storeId", storeId);
        return "headOffice/inventoryStore";  // storeInventory.html
    }

    @PostMapping("/add")
    public String addInventory(@RequestParam String itemCode,
                               @RequestParam Integer storeId,
                               @RequestParam Integer count) {
        inventoryStoreService.insertInventory(itemCode, storeId, count);
        return "redirect:/headOffice/inventoryStore/" + storeId;
    }

    @GetMapping("/headOffice/summary")
    public String showInventorySummary(Model model) {
        List<Store> storeList = storeService.findAll();
        List<InventorySummaryPivotRowDto> pivotList = inventorySummaryService.generateInventoryPivotForAllStores();
        model.addAttribute("storeList", storeList);
        model.addAttribute("pivotList", pivotList);
        return "headOffice/inventorySummary";
    }

    @GetMapping("/byRole")
    public String viewInventoryByStore(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Member loginMember = userDetails.getLoggedMember();
        Role role = loginMember.getRole();

        if (role == Role.ROLE_ADMIN) {
            List<InventorySummaryPivotRowDto> pivotList = inventorySummaryService.generateInventoryPivotForAllStores();
            model.addAttribute("pivotList", pivotList);
            model.addAttribute("storeList", storeService.findAll());
        } else {
            List<MemberStore> msList = memberStoreService.findByMemberId(loginMember.getId());
            if (!msList.isEmpty()) {
                Store store = msList.get(0).getStore();
                Integer storeId = store.getId();
                List<InventorySummaryPivotRowDto> pivotList = inventorySummaryService.generateInventoryPivotByStoreId(storeId);
                model.addAttribute("pivotList", pivotList);
                model.addAttribute("storeList", List.of(store));
            }
        }

        return "headOffice/inventoryStore";
    }


//    @GetMapping("/headOffice/inventoryPivot")
//    public String viewInventoryPivot(Model model) {
//        List<Store> storeList = storeRepository.findAll();
//        List<InventoryItem> itemList = inventoryItemRepository.findAll();
//        List<InventoryPivotDto> pivotList = new ArrayList<>();
//
//        LocalDate threeMonthsAgo = LocalDate.now().minusMonths(3);
//        Date startDate = java.sql.Date.valueOf(threeMonthsAgo);
//        Date endDate = java.sql.Date.valueOf(LocalDate.now());
//
//        for (InventoryItem item : itemList) {
//            InventoryPivotDto dto = new InventoryPivotDto(item.getItemCode(), item.getItemName());
//
//            for (Store store : storeList) {
//                int stock = inventoryStoreRepository.findQuantityByStoreIdAndItemCode(store.getId(), item.getItemCode());
//
//                // 메뉴에서 이 품목을 사용하는 메뉴들 찾기
//                List<Menu> menus = menuRepository.findByItemCode(item.getItemCode());
//                int totalExpectedUsage = 0;
//
//                for (Menu menu : menus) {
//                    Integer qtyUsed = menu.getQuantityUsed();
//                    int qtySold = salesRepository.sumQuantityByStoreIdAndMenuIdAndPeriod(
//                            store.getId(), menu.getMenuId(), startDate, endDate
//                    );
//                    totalExpectedUsage += qtySold * qtyUsed;
//                }
//
//                dto.putStock(store.getId(), stock);
//                dto.putExpectedUsage(store.getId(), totalExpectedUsage);
//            }
//
//            pivotList.add(dto);
//        }
//
//        model.addAttribute("storeList", storeList);
//        model.addAttribute("pivotList", pivotList);
//        return "headOffice/inventoryPivot";
//    }

    @GetMapping
    public String inventoryStorePage(
            @RequestParam(required = false) String searchType,
            @RequestParam(required = false) String itemName,
            @RequestParam(required = false) String storeName,
            Model model
    ) {
        List<Store> storeList = storeService.findAll();
        List<InventorySummaryPivotRowDto> pivotList = inventorySummaryService.generateInventoryPivot(storeList);

        // 🔍 품목명 검색
        if ("itemName".equals(searchType) && itemName != null && !itemName.isBlank()) {
            pivotList = pivotList.stream()
                    .filter(row -> row.getItemName() != null && row.getItemName().contains(itemName))
                    .toList();
        }

        // 🔍 매장명 검색
        if ("storeName".equals(searchType) && storeName != null && !storeName.isBlank()) {
            storeList = storeList.stream()
                    .filter(s -> s.getName() != null && s.getName().contains(storeName))
                    .toList();

            List<Integer> matchedStoreIds = storeList.stream()
                    .map(Store::getId)
                    .toList();

            pivotList = pivotList.stream()
                    .filter(row -> matchedStoreIds.stream().anyMatch(id -> row.getStockByStoreId(id) > 0))
                    .toList();
        }

        model.addAttribute("pivotList", pivotList);
        model.addAttribute("storeList", storeList);
        model.addAttribute("searchType", searchType);
        model.addAttribute("itemName", itemName);
        model.addAttribute("storeName", storeName);

        return "headOffice/inventoryStore";
    }
}