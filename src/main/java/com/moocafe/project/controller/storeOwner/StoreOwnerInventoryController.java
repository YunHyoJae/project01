package com.moocafe.project.controller.storeOwner;

import com.moocafe.project.dto.CustomUserDetails;
import com.moocafe.project.dto.InventorySummaryDto;
import com.moocafe.project.dto.InventorySummaryPivotRowDto;
import com.moocafe.project.entity.Member;
import com.moocafe.project.entity.MemberStore;
import com.moocafe.project.entity.Menu;
import com.moocafe.project.entity.Store;
import com.moocafe.project.repository.MemberStoreRepository;
import com.moocafe.project.repository.MenuRepository;
import com.moocafe.project.repository.SalesRepository;
import com.moocafe.project.service.InventorySummaryService;
import com.moocafe.project.service.MemberStoreService;
import com.moocafe.project.service.MenuService;
import com.moocafe.project.service.SalesService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class StoreOwnerInventoryController {

    private final InventorySummaryService inventorySummaryService;
    private final MemberStoreService memberStoreService;
    private final MenuService menuService;
    private final SalesService salesService;


    @GetMapping("/storeOwner/inventoryStore")
    public String viewStoreOwnerInventory(
            @RequestParam(required = false) String searchType,
            @RequestParam(required = false) String itemName,
            @RequestParam(required = false) String itemCode,
            Model model,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Member loginMember = userDetails.getLoggedMember();
        List<MemberStore> msList = memberStoreService.findByMemberId(loginMember.getId());

        if (!msList.isEmpty()) {
            Store store = msList.get(0).getStore();
            Integer storeId = store.getId();

            List<InventorySummaryDto> rawList = inventorySummaryService.getInventorySummaryByStoreId(storeId);
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

                List<Menu> menus = menuService.findByItemCode(dto.getItemCode());
                int totalExpectedUsage = 0;
                for (Menu menu : menus) {
                    int usedQty = menu.getQuantityUsed();
                    int soldQty = salesService.sumQuantityByStoreIdAndMenuIdAndPeriod(
                            storeId, menu.getMenuId(), startDate, endDate
                    );
                    totalExpectedUsage += usedQty * soldQty;
                }
                row.putExpectedUsage(dto.getStoreId(), totalExpectedUsage);

                // ✅ 3개월 예상 사용량을 3으로 나누고, 반올림해서 저장
                int roundedNeeded = Math.round(totalExpectedUsage / 3.0f); // float 나눗셈 후 반올림
                row.putNeededRounded(dto.getStoreId(), roundedNeeded);
            }

            List<InventorySummaryPivotRowDto> pivotList = new ArrayList<>(pivotMap.values());

            // 🔍 검색 필터
// 🔍 검색 필터
            if ("itemName".equals(searchType) && itemName != null && !itemName.isBlank()) {
                String keyword = itemName.toLowerCase();
                pivotList = pivotList.stream()
                        .filter(row -> row.getItemName() != null && row.getItemName().toLowerCase().contains(keyword))
                        .collect(Collectors.toList());
            }

            if ("itemCode".equals(searchType) && itemCode != null && !itemCode.isBlank()) {
                String keyword = itemCode.toLowerCase();
                pivotList = pivotList.stream()
                        .filter(row -> row.getItemCode() != null && row.getItemCode().toLowerCase().contains(keyword))
                        .collect(Collectors.toList());
            }

            model.addAttribute("pivotList", pivotList);
            model.addAttribute("store", store);
            model.addAttribute("searchType", searchType);
            model.addAttribute("itemName", itemName);
            model.addAttribute("itemCode", itemCode);
        }

        return "storeOwner/inventoryStore";
    }

}
