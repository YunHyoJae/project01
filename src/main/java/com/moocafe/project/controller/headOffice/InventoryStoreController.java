package com.moocafe.project.controller.headOffice;

import com.moocafe.project.constent.Role;
import com.moocafe.project.dto.CustomUserDetails;
import com.moocafe.project.dto.InventoryPivotDto;
import com.moocafe.project.dto.InventorySummaryDto;
import com.moocafe.project.dto.InventorySummaryPivotRowDto;
import com.moocafe.project.entity.*;
import com.moocafe.project.repository.*;
import com.moocafe.project.service.InventoryStoreService;
import com.moocafe.project.service.InventorySummaryService;
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
    private final StoreRepository storeRepository;
    private final MemberStoreRepository memberStoreRepository;
    private final InventoryItemRepository inventoryItemRepository;
    private final InventoryStoreRepository inventoryStoreRepository;
    private final MenuRepository menuRepository;
    private final SalesRepository salesRepository;
    private final StoreService storeService;

    @GetMapping("/{storeId}")
    public String viewStoreInventory(@PathVariable Integer storeId, Model model) {
        List<InventoryStore> inventory = inventoryStoreService.getInventoryByStore(storeId);
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
        List<InventorySummaryDto> rawList = inventorySummaryService.getInventorySummary();

        // itemCode + itemName 기준으로 Pivot 구성
        Map<String, InventorySummaryPivotRowDto> pivotMap = new LinkedHashMap<>();

        for (InventorySummaryDto dto : rawList) {
            String key = dto.getItemCode() + "::" + dto.getItemName();
            pivotMap.putIfAbsent(key, new InventorySummaryPivotRowDto());
            InventorySummaryPivotRowDto row = pivotMap.get(key);

            row.setItemCode(dto.getItemCode());
            row.setItemName(dto.getItemName());
            row.getStoreStockMap().put(dto.getStoreId(), dto.getTotalCount().intValue());
        }
        model.addAttribute("storeList", storeRepository.findAll());
        model.addAttribute("pivotList", pivotMap.values());
        return "headOffice/inventorySummary";
    }

//    @GetMapping
//    public String viewAllInventoryForAdmin(Model model) {
//        List<InventorySummaryDto> rawList = inventorySummaryService.getInventorySummary();
//        Map<String, InventorySummaryPivotRowDto> pivotMap = new LinkedHashMap<>();
//
//        // 🔽 예상 사용량 계산용 날짜 범위
//        LocalDate threeMonthsAgo = LocalDate.now().minusMonths(3);
//        Date startDate = java.sql.Date.valueOf(threeMonthsAgo);
//        Date endDate = java.sql.Date.valueOf(LocalDate.now());
//
//        for (InventorySummaryDto dto : rawList) {
//            String key = dto.getItemCode() + "::" + dto.getItemName();
//            pivotMap.putIfAbsent(key, new InventorySummaryPivotRowDto());
//            InventorySummaryPivotRowDto row = pivotMap.get(key);
//
//            row.setItemCode(dto.getItemCode());
//            row.setItemName(dto.getItemName());
//            row.getStoreStockMap().put(dto.getStoreId(), dto.getTotalCount().intValue());
//
//            // 🔽 추가: 예상 사용량 계산
//            List<Menu> menus = menuRepository.findByItemCode(dto.getItemCode());
//            int totalExpectedUsage = 0;
//            for (Menu menu : menus) {
//                int usedQty = menu.getQuantityUsed();
//                int soldQty = salesRepository.sumQuantityByStoreIdAndMenuIdAndPeriod(
//                        dto.getStoreId(), menu.getMenuId(), startDate, endDate
//                );
//                totalExpectedUsage += usedQty * soldQty;
//            }
//            row.putExpectedUsage(dto.getStoreId(), totalExpectedUsage);  // 🔑 저장
//        }
//
//        model.addAttribute("storeList", storeRepository.findAll());
//        model.addAttribute("pivotList", pivotMap.values());
//        return "headOffice/inventoryStore";
//    }

    @GetMapping("/byRole")
    public String viewInventoryByStore(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Member loginMember = userDetails.getLoggedMember();
        Role role = loginMember.getRole();

        if (role == Role.ROLE_ADMIN) {
            // 관리자: 전체 매장 조회
            List<InventorySummaryDto> rawList = inventorySummaryService.getInventorySummary();
            Map<String, InventorySummaryPivotRowDto> pivotMap = new LinkedHashMap<>();

            for (InventorySummaryDto dto : rawList) {
                String key = dto.getItemCode() + "::" + dto.getItemName();
                pivotMap.putIfAbsent(key, new InventorySummaryPivotRowDto());
                InventorySummaryPivotRowDto row = pivotMap.get(key);
                row.setItemCode(dto.getItemCode());
                row.setItemName(dto.getItemName());
                row.getStoreStockMap().put(dto.getStoreId(), dto.getTotalCount().intValue());
            }

            model.addAttribute("pivotList", pivotMap.values());
            model.addAttribute("storeList", storeRepository.findAll());
        } else {
            // 일반 사용자 또는 매니저: 본인 매장만 조회
            List<MemberStore> msList = memberStoreRepository.findByMemberId(loginMember.getId());
            if (!msList.isEmpty()) {
                Store store = msList.get(0).getStore(); // 첫 번째 매장 기준
                Integer storeId = store.getId();

                List<InventorySummaryDto> rawList = inventorySummaryService.getInventorySummaryByStoreId(storeId);
                Map<String, InventorySummaryPivotRowDto> pivotMap = new LinkedHashMap<>();

                for (InventorySummaryDto dto : rawList) {
                    String key = dto.getItemCode() + "::" + dto.getItemName();
                    pivotMap.putIfAbsent(key, new InventorySummaryPivotRowDto());
                    InventorySummaryPivotRowDto row = pivotMap.get(key);
                    row.setItemCode(dto.getItemCode());
                    row.setItemName(dto.getItemName());
                    row.getStoreStockMap().put(dto.getStoreId(), dto.getTotalCount().intValue());
                }

                model.addAttribute("pivotList", pivotMap.values());
                model.addAttribute("storeList", List.of(store));
            }
        }

        return "headOffice/inventoryStore";
    }

    @GetMapping("/storeOwner/inventoryStore")
    public String viewStoreOwnerInventory(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Member loginMember = userDetails.getLoggedMember();
        List<MemberStore> msList = memberStoreRepository.findByMemberId(loginMember.getId());

        if (!msList.isEmpty()) {
            Store store = msList.get(0).getStore();  // 점주가 여러 매장 소유 시 첫 번째 기준
            Integer storeId = store.getId();

            List<InventorySummaryDto> rawList = inventorySummaryService.getInventorySummaryByStoreId(storeId);
            Map<String, InventorySummaryPivotRowDto> pivotMap = new LinkedHashMap<>();

            for (InventorySummaryDto dto : rawList) {
                String key = dto.getItemCode() + "::" + dto.getItemName();
                pivotMap.putIfAbsent(key, new InventorySummaryPivotRowDto());
                InventorySummaryPivotRowDto row = pivotMap.get(key);
                row.setItemCode(dto.getItemCode());
                row.setItemName(dto.getItemName());
                row.getStoreStockMap().put(dto.getStoreId(), dto.getTotalCount().intValue());
            }

            model.addAttribute("pivotList", pivotMap.values());
            model.addAttribute("store", store);  // store.name 등 템플릿에서 사용 가능
        }

        return "storeOwner/inventoryStore";  // 점주용 템플릿
    }

    @GetMapping("/headOffice/inventoryPivot")
    public String viewInventoryPivot(Model model) {
        List<Store> storeList = storeRepository.findAll();
        List<InventoryItem> itemList = inventoryItemRepository.findAll();
        List<InventoryPivotDto> pivotList = new ArrayList<>();

        LocalDate threeMonthsAgo = LocalDate.now().minusMonths(3);
        Date startDate = java.sql.Date.valueOf(threeMonthsAgo);
        Date endDate = java.sql.Date.valueOf(LocalDate.now());

        for (InventoryItem item : itemList) {
            InventoryPivotDto dto = new InventoryPivotDto(item.getItemCode(), item.getItemName());

            for (Store store : storeList) {
                int stock = inventoryStoreRepository.findQuantityByStoreIdAndItemCode(store.getId(), item.getItemCode());

                // 메뉴에서 이 품목을 사용하는 메뉴들 찾기
                List<Menu> menus = menuRepository.findByItemCode(item.getItemCode());
                int totalExpectedUsage = 0;

                for (Menu menu : menus) {
                    Integer qtyUsed = menu.getQuantityUsed();
                    int qtySold = salesRepository.sumQuantityByStoreIdAndMenuIdAndPeriod(
                            store.getId(), menu.getMenuId(), startDate, endDate
                    );
                    totalExpectedUsage += qtySold * qtyUsed;
                }

                dto.putStock(store.getId(), stock);
                dto.putExpectedUsage(store.getId(), totalExpectedUsage);
            }

            pivotList.add(dto);
        }

        model.addAttribute("storeList", storeList);
        model.addAttribute("pivotList", pivotList);
        return "headOffice/inventoryPivot";
    }

    @GetMapping
    public String inventoryStorePage(
            @RequestParam(required = false) String searchType,
            @RequestParam(required = false) String itemName,
            @RequestParam(required = false) String storeName,
            Model model
    ) {
        List<InventorySummaryDto> rawList = inventorySummaryService.getInventorySummary();
        Map<String, InventorySummaryPivotRowDto> pivotMap = new LinkedHashMap<>();

        // 🔽 3개월 예상 사용량 계산용 날짜 범위
        LocalDate threeMonthsAgo = LocalDate.now().minusMonths(3);
        Date startDate = java.sql.Date.valueOf(threeMonthsAgo);
        Date endDate = java.sql.Date.valueOf(LocalDate.now());

        for (InventorySummaryDto dto : rawList) {
            String key = dto.getItemCode() + "::" + dto.getItemName();
            pivotMap.putIfAbsent(key, new InventorySummaryPivotRowDto());
            InventorySummaryPivotRowDto row = pivotMap.get(key);

            row.setItemCode(dto.getItemCode());
            row.setItemName(dto.getItemName());
            row.getStoreStockMap().put(dto.getStoreId(), dto.getTotalCount().intValue());

            // 🔽 예상 사용량 계산
            List<Menu> menus = menuRepository.findByItemCode(dto.getItemCode());
            int totalExpectedUsage = 0;
            for (Menu menu : menus) {
                int usedQty = menu.getQuantityUsed();
                int soldQty = salesRepository.sumQuantityByStoreIdAndMenuIdAndPeriod(
                        dto.getStoreId(), menu.getMenuId(), startDate, endDate
                );
                totalExpectedUsage += usedQty * soldQty;
            }
            row.putExpectedUsage(dto.getStoreId(), totalExpectedUsage);
        }

        List<InventorySummaryPivotRowDto> pivotList = new ArrayList<>(pivotMap.values());
        List<Store> allStores = storeService.findAll();
        List<Store> storeList = allStores;

        // 🔍 품목명 검색
        if ("itemName".equals(searchType) && itemName != null && !itemName.isBlank()) {
            pivotList = pivotList.stream()
                    .filter(row -> row.getItemName() != null && row.getItemName().contains(itemName))
                    .collect(Collectors.toList());
        }

        // 🔍 매장명 검색
        if ("storeName".equals(searchType) && storeName != null && !storeName.isBlank()) {
            storeList = allStores.stream()
                    .filter(s -> s.getName() != null && s.getName().contains(storeName))
                    .toList();

            List<Integer> matchedStoreIds = storeList.stream()
                    .map(Store::getId)
                    .toList();

            pivotList = pivotList.stream()
                    .filter(row -> matchedStoreIds.stream().anyMatch(id -> row.getStockByStoreId(id) > 0))
                    .collect(Collectors.toList());
        }

        model.addAttribute("pivotList", pivotList);
        model.addAttribute("storeList", storeList);
        model.addAttribute("searchType", searchType);
        model.addAttribute("itemName", itemName);
        model.addAttribute("storeName", storeName);

        return "headOffice/inventoryStore";
    }



}