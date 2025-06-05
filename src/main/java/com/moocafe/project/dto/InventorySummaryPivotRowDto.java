package com.moocafe.project.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class InventorySummaryPivotRowDto {
    private String itemCode;
    private String itemName;

    // 매장ID → 수량
    private Map<Integer, Integer> storeStockMap = new HashMap<>();

    public int getStockByStoreId(int storeId) {
        return storeStockMap.getOrDefault(storeId, 0);
    }

    public String getStockCssClass(int storeId) {
        int count = getStockByStoreId(storeId);
        return count < 5 ? "low-stock" : "";
    }
}
