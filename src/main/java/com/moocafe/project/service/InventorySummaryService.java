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
    private final StoreRepository storeRepository;

    public InventorySummaryService(InventoryStoreDao inventoryStoreDao, InventoryStoreRepository inventoryStoreRepository, InventoryItemRepository inventoryItemRepository, MenuRepository menuRepository, SalesRepository salesRepository, StoreRepository storeRepository) {
        this.inventoryStoreDao = inventoryStoreDao;
        this.inventoryStoreRepository = inventoryStoreRepository;
        this.inventoryItemRepository = inventoryItemRepository;
        this.menuRepository = menuRepository;
        this.salesRepository = salesRepository;
        this.storeRepository = storeRepository;
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

    public List<InventorySummaryPivotRowDto> getShortageListByStoreId(Integer storeId) {
        // 1. 해당 매장 Store 객체 가져오기
        Store store = storeRepository.findById(storeId).orElse(null);
        if (store == null) {
            // storeId가 잘못됐을 때 예외 처리 (원하는 방식으로)
            return Collections.emptyList();
        }
        List<Store> storeList = new ArrayList<>();
        storeList.add(store);

        // 2. pivot row 전체 생성
        List<InventorySummaryPivotRowDto> pivotList = generateInventoryPivot(storeList);

        // 3. 부족재고만 필터
        List<InventorySummaryPivotRowDto> shortageList = new ArrayList<>();
        for (InventorySummaryPivotRowDto row : pivotList) {
            Integer stock = row.getStockByStoreId(storeId);
            Integer needed = row.getNeededRoundedByStoreId(storeId); // 최근 3개월 평균 등
            if (stock != null && needed != null && stock < needed) {
                shortageList.add(row);
            }
        }
        return shortageList;
    }

    // 전체 매장 pivot: 재고+예상사용량
    public List<InventorySummaryPivotRowDto> generateInventoryPivotForAllStores() {
        List<Store> storeList = storeRepository.findAll();
        return generateInventoryPivot(storeList);
    }

    // 특정 매장 pivot: 재고+예상사용량
    public List<InventorySummaryPivotRowDto> generateInventoryPivotByStoreId(Integer storeId) {
        Store store = storeRepository.findById(storeId).orElse(null);
        if (store == null) return Collections.emptyList();
        return generateInventoryPivot(List.of(store));
    }






}
