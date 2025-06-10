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

        // 👉 3개월치 예상 사용량을 3으로 나누어 1개월 사용량 기준으로 비교
        int expectedMonthly = (int) Math.ceil(expectedThreeMonth / 3.0);

        return stock < expectedMonthly ? "low-stock" : "";
    }
}
