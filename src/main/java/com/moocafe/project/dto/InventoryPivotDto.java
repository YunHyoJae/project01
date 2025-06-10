package com.moocafe.project.dto;

import java.util.HashMap;
import java.util.Map;

public class InventoryPivotDto {
    private String itemCode;
    private String itemName;

    // 매장별 현재 재고
    private Map<Integer, Integer> stockByStoreId = new HashMap<>();

    // 매장별 예상 사용량 (최근 3개월)
    private Map<Integer, Integer> expectedUsageByStoreId = new HashMap<>();

    public InventoryPivotDto(String itemCode, String itemName) {
        this.itemCode = itemCode;
        this.itemName = itemName;
    }

    public void putStock(int storeId, int quantity) {
        stockByStoreId.put(storeId, quantity);
    }

    public void putExpectedUsage(int storeId, int quantity) {
        expectedUsageByStoreId.put(storeId, quantity);
    }

    public int getStockByStoreId(int storeId) {
        return stockByStoreId.getOrDefault(storeId, 0);
    }

    public int getExpectedUsageByStoreId(int storeId) {
        return expectedUsageByStoreId.getOrDefault(storeId, 0);
    }

    public String getStockCssClass(int storeId) {
        int stock = getStockByStoreId(storeId);
        int expected = getExpectedUsageByStoreId(storeId);
        if (expected > 0 && stock < expected) {
            return "low-stock";
        }
        return "";
    }

    public String getItemCode() {
        return itemCode;
    }

    public String getItemName() {
        return itemName;
    }
}
