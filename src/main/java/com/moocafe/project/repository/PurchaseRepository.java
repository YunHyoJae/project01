package com.moocafe.project.repository;

import com.moocafe.project.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {
    @Modifying
    @Transactional
    @Query(value = """
        MERGE INTO inventoryStore s
        USING (
            SELECT itemCode, receivedQuantity
            FROM purchaseItem
            WHERE purchaseNumber = :purchaseNumber
        ) p
        ON (s.itemCode = p.itemCode AND s.storeId = 1)
        WHEN MATCHED THEN
            UPDATE SET s.count = s.count + p.receivedQuantity
        """, nativeQuery = true)
    void updateInventoryByPurchase(@Param("purchaseNumber") String purchaseNumber);

    Optional<Purchase> findByPurchaseNumber(String purchaseNumber);
}
