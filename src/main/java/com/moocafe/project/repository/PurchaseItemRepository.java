package com.moocafe.project.repository;

import com.moocafe.project.entity.InventoryItem;
import com.moocafe.project.entity.Purchase;
import com.moocafe.project.entity.PurchaseItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PurchaseItemRepository extends JpaRepository<PurchaseItem, Integer> {
    List<PurchaseItem> findByPurchaseNumber(Purchase purchase);

    Optional<PurchaseItem> findByPurchaseNumberAndItemCode(Purchase purchaseNumber, InventoryItem itemCode);

    @Query(
            value = "SELECT p.id, p.itemCode, i.itemName, p.receivedQuantity, p.dueDate, p.expirationDate, p.supplier, p.status " +
                    "FROM PURCHASEITEM p " +
                    "JOIN INVENTORYREGISTRATION i ON p.itemCode = i.itemCode " +
                    "ORDER BY p.dueDate DESC " +
                    "FETCH FIRST 4 ROWS ONLY",
            nativeQuery = true
    )
    List<Object[]> findRecent4();
}
