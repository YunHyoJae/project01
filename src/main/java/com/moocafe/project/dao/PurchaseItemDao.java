package com.moocafe.project.dao;

import com.moocafe.project.dto.PurchaseItemDto;
import com.moocafe.project.entity.InventoryItem;
import com.moocafe.project.entity.Purchase;
import com.moocafe.project.entity.PurchaseItem;
import com.moocafe.project.repository.PurchaseItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PurchaseItemDao {
    private final PurchaseItemRepository purchaseItemRepository;

    public PurchaseItem savePurchaseItem(PurchaseItem purchaseItem) {
        return purchaseItemRepository.save(purchaseItem);
    }

    public List<PurchaseItem> findItemByPurchaseNumber(Purchase purchase) {
        return purchaseItemRepository.findByPurchaseNumber(purchase);
    }

    public Optional<PurchaseItem> findByPurchaseNumberAndItemCode(Purchase purchaseNumber, InventoryItem itemCode) {
        return purchaseItemRepository.findByPurchaseNumberAndItemCode(purchaseNumber, itemCode);
    }


}
