package com.moocafe.project.service;

import com.moocafe.project.dto.*;
import com.moocafe.project.entity.InventoryItem;
import com.moocafe.project.entity.Menu;
import com.moocafe.project.entity.OutBound;
import com.moocafe.project.entity.Store;
import com.moocafe.project.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HeadIndexService {
    private final SalesRepository salesRepository;
    private final InventoryStoreRepository inventoryStoreRepository;
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    private final OutBoundRepository outBoundRepository;
    private final InventoryItemRepository inventoryItemRepository;
    private final PurchaseItemRepository purchaseItemRepository;

    public HeadIndexDashboardDto getDashboard() {
        // 1. 매출 차트용 summaryList
        List<SalesSummaryDto> summaryList = salesRepository.findSalesSummaryByDate(null, null, null);

        // 2. pivotList(재고 요약) 생성
        List<InventorySummaryDto> rawList = inventoryStoreRepository.getInventorySummary();
        Map<String, InventorySummaryPivotRowDto> pivotMap = new LinkedHashMap<>();

        // 3개월 예상 사용량 계산용 날짜 범위
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

            // 예상 사용량 계산
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
        List<Store> storeList = storeRepository.findAll();

        // 3. 부족재고 리스트
        List<InventoryShortageDto> shortageList = new ArrayList<>();
        for (InventorySummaryPivotRowDto row : pivotList) {
            for (Store store : storeList) {
                String css = row.getStockCssClass(store.getId());
                int stock = row.getStockByStoreId(store.getId());
                if ("low-stock".equals(css)) {
                    shortageList.add(new InventoryShortageDto(
                            row.getItemName(),
                            store.getName(),
                            stock
                    ));
                }
            }
        }

        // 4. 출고리스트 최근 4개
        Map<Integer, String> storeIdNameMap = storeList.stream()
                .collect(Collectors.toMap(Store::getId, Store::getName));

        List<InventoryItem> items = inventoryItemRepository.findAll();
        Map<String, String> itemCodeNameMap = items.stream()
                .collect(Collectors.toMap(InventoryItem::getItemCode, InventoryItem::getItemName));

        List<Object[]> outBoundRawList = outBoundRepository.findRecentOutBoundListRaw();
        List<OutBoundListResponseDto> recentOutbounds = outBoundRawList.stream()
                .map(arr -> new OutBoundListResponseDto(
                        (Integer) arr[0],
                        (String) arr[1],
                        (String) arr[2],
                        (String) arr[3],
                        arr[4] != null ? ((Number) arr[4]).intValue() : null,
                        (String) arr[5],
                        (String) arr[6],
                        (String) arr[7]
                ))
                .collect(Collectors.toList());

        // 5. 입고리스트 최근 4개
        List<Object[]> recentItems = purchaseItemRepository.findRecent4();
        List<HeadPurchaseItemDto> recentPurchaseItems = recentItems.stream()
                .map(arr -> new HeadPurchaseItemDto(
                        arr[0] == null ? null : Integer.valueOf(arr[0].toString()),
                        arr[1] == null ? null : arr[1].toString(),
                        arr[2] == null ? null : arr[2].toString(),
                        arr[3] == null ? null : Integer.valueOf(arr[3].toString()),
                        arr[4] == null ? null : ((Date) arr[4]).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                        arr[5] == null ? null : ((Date) arr[5]).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                        arr[6] == null ? null : arr[6].toString(),
                        arr[7] == null ? null : arr[7].toString()
                ))
                .collect(Collectors.toList());

        return HeadIndexDashboardDto.builder()
                .summaryList(summaryList)
                .pivotList(pivotList)
                .shortageList(shortageList)
                .recentOutbounds(recentOutbounds)
                .recentPurchaseItems(recentPurchaseItems)
                .storeIdNameMap(storeIdNameMap)
                .itemCodeNameMap(itemCodeNameMap)
                .build();
    }
}