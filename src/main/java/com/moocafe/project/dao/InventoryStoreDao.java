package com.moocafe.project.dao;

import com.moocafe.project.dto.InventorySummaryDto;
import com.moocafe.project.repository.InventoryStoreRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class InventoryStoreDao {

    private final InventoryStoreRepository inventoryStoreRepository;

    @PersistenceContext
    private EntityManager em;

    public List<InventorySummaryDto> getStoreInventorySummary() {
        String sql = """
                SELECT i.itemCode, r.itemName, i.storeId, SUM(NVL(i.count, 0)) AS totalCount
                FROM InventoryStore i
                JOIN InventoryRegistration r ON i.itemCode = r.itemCode
                GROUP BY i.itemCode, r.itemName, i.storeId
                ORDER BY i.itemCode, i.storeId
                """;

        return em.createNativeQuery(sql, "InventorySummaryMapping").getResultList();
    }

//    public void decreaseStoreStockAllByReturnNumber(String returnNumber) {
//        inventoryStoreRepository.decreaseStoreStockAllByReturnNumber(returnNumber);
//    }

    // 매장 재고 감소
    public void decreaseStoreStockByReturnNumber(String returnNumber) {
        inventoryStoreRepository.decreaseStoreStockByReturnNumber(returnNumber);
    };

    // 본사 재고 증가
    public void increaseHQStock(String itemCode, int quantity) {
        inventoryStoreRepository.increaseHQStock(itemCode, quantity);
    }; // storeId = 1로 고정 처리

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void decreaseStoreStock(Integer storeId, String itemCode, int quantity) {
        inventoryStoreRepository.decreaseStock(storeId, itemCode, quantity);
    }

}