package com.moocafe.project.controller.storeOwner;

import com.moocafe.project.dto.CustomUserDetails;
import com.moocafe.project.dto.InventorySummaryDto;
import com.moocafe.project.dto.InventorySummaryPivotRowDto;
import com.moocafe.project.entity.Member;
import com.moocafe.project.entity.MemberStore;
import com.moocafe.project.entity.Store;
import com.moocafe.project.repository.MemberStoreRepository;
import com.moocafe.project.service.InventorySummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
@RequestMapping("/storeOwner/inventoryStore")
@RequiredArgsConstructor
public class StoreOwnerInventoryController {

    private final InventorySummaryService inventorySummaryService;
    private final MemberStoreRepository memberStoreRepository;

    @GetMapping
    public String viewStoreOwnerInventory(Model model,
                                          @AuthenticationPrincipal CustomUserDetails userDetails) {
        Member loginMember = userDetails.getLoggedMember();
        List<MemberStore> msList = memberStoreRepository.findByMemberId(loginMember.getId());

        if (!msList.isEmpty()) {
            Store store = msList.get(0).getStore();  // 다수 매장 중 첫 번째 사용
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
            model.addAttribute("store", store);
        }

        return "storeOwner/inventoryStore";
    }
}
