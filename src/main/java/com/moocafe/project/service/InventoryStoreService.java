package com.moocafe.project.service;

import com.moocafe.project.dto.InventorySummaryDto;
import com.moocafe.project.dto.InventorySummaryPivotRowDto;
import com.moocafe.project.dto.ItemSearchDto;
import com.moocafe.project.entity.InventoryItem;
import com.moocafe.project.entity.InventoryStore;
import com.moocafe.project.entity.Store;
import com.moocafe.project.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@RequiredArgsConstructor
public class InventoryStoreService {

    private final InventoryStoreRepository inventoryStoreRepository;
    private final InventoryItemRepository inventoryItemRepository;
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    private final SalesRepository salesRepo;

    public List<InventoryStore> getInventoryByStore(Integer storeId) {
        return inventoryStoreRepository.findByStoreId(storeId);
    }

    public void insertInventory(String itemCode, Integer storeId, Integer count) {
        InventoryStore store = new InventoryStore(itemCode, storeId, count, new Date());
        inventoryStoreRepository.save(store);
    }

    public void updateCount(Long id, Integer newCount) {
        InventoryStore store = inventoryStoreRepository.findById(id).orElseThrow();
        store.updateCount(newCount, new Date());
        inventoryStoreRepository.save(store);
    }

    /**
     * 컨트롤러에서 repository 직접 접근하지 않고 사용하도록 추가
     */
    public List<ItemSearchDto> getItemListByStoreId(Integer storeId) {
        List<InventoryStore> storeList = inventoryStoreRepository.findByStoreId(storeId);
        List<ItemSearchDto> result = new ArrayList<>();
        for (InventoryStore store : storeList) {
            InventoryItem item = inventoryItemRepository.findByItemCode(store.getItemCode())
                    .orElseThrow(() -> new IllegalArgumentException("해당 itemCode에 대한 기초 품목 정보가 없습니다: " + store.getItemCode()));
            result.add(new ItemSearchDto(
                    item.getItemCode(),
                    item.getItemName(),
                    item.getItemPrice(),
                    store.getCount()
            ));
        }
        return result;
    }

    public Optional<InventoryItem> findInventoryItemByItemCode(String itemCode) {
        return inventoryItemRepository.findByItemCode(itemCode);
    }

    public int findQuantityByStoreIdAndItemCode(Integer storeId, String itemCode) {
        Integer qty = inventoryStoreRepository.findQuantityByStoreIdAndItemCode(storeId, itemCode);
        return (qty != null) ? qty : 0;
    }

    public List<InventorySummaryPivotRowDto> generateInventoryPivot() {
        List<Store> stores = storeRepository.findAll();
        List<InventoryStore> inventories = inventoryStoreRepository.findAll();
        List<InventoryItem> items = inventoryItemRepository.findAll();

        // 결과 담을 map (itemCode 기준)
        Map<String, InventorySummaryPivotRowDto> pivotMap = new LinkedHashMap<>();

        for (InventoryItem item : items) {
            String itemCode = item.getItemCode();
            String itemName = item.getItemName();

            InventorySummaryPivotRowDto row = new InventorySummaryPivotRowDto();
            row.setItemCode(itemCode);
            row.setItemName(itemName);

            // 매장별 현재 재고 수량
            for (Store store : stores) {
                int count = inventories.stream()
                        .filter(inv -> inv.getItemCode().equals(itemCode) && inv.getStoreId().equals(store.getId()))
                        .mapToInt(InventoryStore::getCount)
                        .sum();
                row.getStoreStockMap().put(store.getId(), count);

                // 예상 사용량: 최근 3개월간 판매량
                int usage = menuRepository.findByItemCode(itemCode).stream()
                        .mapToInt(menu -> salesRepo.sumQuantityByStoreIdAndMenuIdAndPeriod(
                                store.getId(), menu.getMenuId(),
                                getMonthAgoDate(3), new Date()
                        )).sum();

                row.putExpectedUsage(store.getId(), usage);
            }

            pivotMap.put(itemCode, row);
        }

        return new ArrayList<>(pivotMap.values());
    }

    private Date getMonthAgoDate(int monthsAgo) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -monthsAgo);
        return cal.getTime();
    }

    public void decreaseStock(Integer storeId, String itemCode, int usedQty) {
        inventoryStoreRepository.decreaseStock(storeId, itemCode, usedQty);
    }

    public List<InventorySummaryDto> getInventorySummary() {
        return inventoryStoreRepository.getInventorySummary();
    }


}