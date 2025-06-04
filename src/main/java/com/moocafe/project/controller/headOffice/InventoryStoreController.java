package com.moocafe.project.controller.headOffice;

import com.moocafe.project.dto.InventorySummaryDto;
import com.moocafe.project.dto.InventorySummaryPivotRowDto;
import com.moocafe.project.entity.InventoryStore;
import com.moocafe.project.service.InventoryStoreService;
import com.moocafe.project.service.InventorySummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/inventory/store")
@RequiredArgsConstructor
public class InventoryStoreController {

    private final InventoryStoreService service;
    private final InventorySummaryService inventorySummaryService;

    @GetMapping("/{storeId}")
    public String viewStoreInventory(@PathVariable Integer storeId, Model model) {
        List<InventoryStore> inventory = service.getInventoryByStore(storeId);
        model.addAttribute("inventoryList", inventory);
        model.addAttribute("storeId", storeId);
        return "inventory/storeInventory";  // storeInventory.html
    }

    @PostMapping("/add")
    public String addInventory(@RequestParam String itemCode,
                               @RequestParam Integer storeId,
                               @RequestParam Integer count) {
        service.insertInventory(itemCode, storeId, count);
        return "redirect:/inventory/store/" + storeId;
    }

    @GetMapping("/inventory/summary")
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
            row.getStoreStockMap().put(dto.getStoreId(), dto.getTotalCount());
        }

        model.addAttribute("pivotList", pivotMap.values());
        return "inventory/inventorySummary";
    }
}