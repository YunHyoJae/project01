package com.moocafe.project.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class InventorySummaryPivotRowDto {
    private String itemCode;
    private String itemName;

    // 매장ID → 현재 재고 수량
    private Map<Integer, Integer> storeStockMap = new HashMap<>();

    // 매장ID → 3개월간 예상 사용량
    private Map<Integer, Integer> expectedUsageMap = new HashMap<>();

    // 현재 재고 수량 가져오기
    public int getStockByStoreId(int storeId) {
        return storeStockMap.getOrDefault(storeId, 0);
    }

    // 예상 사용량 저장
    public void putExpectedUsage(int storeId, int usage) {
        expectedUsageMap.put(storeId, usage);
    }

    // 예상 사용량 가져오기
    public int getExpectedUsageByStoreId(int storeId) {
        return expectedUsageMap.getOrDefault(storeId, 0);
    }

    // 재고 부족 시 스타일 적용
    public String getStockCssClass(int storeId) {

        int stock = getStockByStoreId(storeId);
        int expectedThreeMonth = getExpectedUsageByStoreId(storeId);
        int expectedMonthly = (int) Math.ceil(expectedThreeMonth / 3.0);
        if (expectedMonthly == 0) {
            return "";
        }
        return stock < expectedMonthly ? "low-stock" : "";
    }


    private Map<Integer, Integer> neededRounded = new HashMap<>();
    public void putNeededRounded(Integer storeId, Integer value) {
        neededRounded.put(storeId, value);
    }
    public Integer getNeededRoundedByStoreId(Integer storeId) {
        return neededRounded.getOrDefault(storeId, 0);
    }

}
