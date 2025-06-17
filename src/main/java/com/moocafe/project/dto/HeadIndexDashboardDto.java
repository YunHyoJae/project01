package com.moocafe.project.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class HeadIndexDashboardDto {
    private List<SalesSummaryDto> summaryList;
    private List<InventorySummaryPivotRowDto> pivotList;
    private List<InventoryShortageDto> shortageList;
    private List<OutBoundListResponseDto> recentOutbounds;
    private List<HeadPurchaseItemDto> recentPurchaseItems;
    private Map<Integer, String> storeIdNameMap;
    private Map<String, String> itemCodeNameMap;
}