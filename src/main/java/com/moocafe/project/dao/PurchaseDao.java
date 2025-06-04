package com.moocafe.project.dao;

import com.moocafe.project.entity.Purchase;
import com.moocafe.project.repository.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PurchaseDao {
    private PurchaseRepository purchaseRepository;

    public Purchase savePurchase (Purchase purchase) {
        return purchaseRepository.save(purchase);
    }
}
