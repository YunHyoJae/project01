package com.moocafe.project.repository;

import com.moocafe.project.entity.InventoryItem;
import com.moocafe.project.entity.Purchase;
import com.moocafe.project.entity.PurchaseItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PurchaseItemRepository extends JpaRepository<PurchaseItem, Integer> {
    List<PurchaseItem> findByPurchaseNumber(Purchase purchase);

    Optional<PurchaseItem> findByPurchaseNumberAndItemCode(Purchase purchaseNumber, InventoryItem itemCode);

}
