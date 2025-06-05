package com.moocafe.project.dao;

import com.moocafe.project.entity.PurchaseItem;
import com.moocafe.project.repository.PurchaseItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PurchaseItemDao {
    private final PurchaseItemRepository purchaseItemRepository;

    public PurchaseItem savePurchaseItem(PurchaseItem purchaseItem) {
        return purchaseItemRepository.save(purchaseItem);
    }

}
