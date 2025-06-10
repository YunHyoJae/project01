package com.moocafe.project.service;

import com.moocafe.project.dao.InventoryStoreDao;
import com.moocafe.project.dto.InventorySummaryDto;
import com.moocafe.project.dto.InventorySummaryPivotRowDto;
import com.moocafe.project.entity.InventoryItem;
import com.moocafe.project.entity.InventoryStore;
import com.moocafe.project.entity.Menu;
import com.moocafe.project.entity.Store;
import com.moocafe.project.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class InventorySummaryService {

    private final InventoryStoreDao inventoryStoreDao;
    private final InventoryStoreRepository inventoryStoreRepository;
    private final InventoryItemRepository inventoryItemRepository;
    private final MenuRepository menuRepository;
    private final SalesRepository salesRepository;

    public InventorySummaryService(InventoryStoreDao inventoryStoreDao, InventoryStoreRepository inventoryStoreRepository, InventoryItemRepository inventoryItemRepository, MenuRepository menuRepository, SalesRepository salesRepository) {
        this.inventoryStoreDao = inventoryStoreDao;
        this.inventoryStoreRepository = inventoryStoreRepository;
        this.inventoryItemRepository = inventoryItemRepository;
        this.menuRepository = menuRepository;
        this.salesRepository = salesRepository;
    }

    public List<InventorySummaryDto> getInventorySummary() {
        return inventoryStoreRepository.getInventorySummary();
    }

    public List<InventorySummaryDto> getInventorySummaryByStoreId(Integer storeId) {
        return inventoryStoreRepository.getInventorySummaryByStoreId(storeId);
    }

    public List<InventorySummaryPivotRowDto> generateInventoryPivot(List<Store> storeList) {
        Map<String, InventorySummaryPivotRowDto> dtoMap = new LinkedHashMap<>();

        // 1. 품목명 불러오기
        Map<String, String> itemNameMap = new HashMap<>();
        for (InventoryItem reg : inventoryItemRepository.findAll()) {
            itemNameMap.put(reg.getItemCode(), reg.getItemName());
        }

        // 2. 재고 수량 채우기
        for (InventoryStore inv : inventoryStoreRepository.findAll()) {
            String itemCode = inv.getItemCode();
            int storeId = inv.getStoreId();

            dtoMap.putIfAbsent(itemCode, new InventorySummaryPivotRowDto());
            InventorySummaryPivotRowDto dto = dtoMap.get(itemCode);
            dto.setItemCode(itemCode);
            dto.setItemName(itemNameMap.getOrDefault(itemCode, ""));

            dto.getStoreStockMap().put(storeId, dto.getStoreStockMap().getOrDefault(storeId, 0) + inv.getCount());
        }

        // 3. 예상 사용량 계산 (최근 3개월)
        LocalDate now = LocalDate.now();
        LocalDate threeMonthsAgo = now.minusMonths(3);

        for (Menu menu : menuRepository.findAll()) {
            String itemCode = menu.getItemCode();
            if (itemCode == null || !dtoMap.containsKey(itemCode)) continue;

            InventorySummaryPivotRowDto dto = dtoMap.get(itemCode);

            for (Store store : storeList) {
                Date start = java.sql.Date.valueOf(threeMonthsAgo);
                Date end = java.sql.Date.valueOf(now);
                int usedQty = salesRepository.sumQuantityByStoreIdAndMenuIdAndPeriod(store.getId(), menu.getMenuId(), start, end);

                int totalUsed = usedQty * menu.getQuantityUsed();
                dto.putExpectedUsage(store.getId(),
                        dto.getExpectedUsageByStoreId(store.getId()) + totalUsed);
            }
        }

        return new ArrayList<>(dtoMap.values());
    }

}
