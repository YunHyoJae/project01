package com.moocafe.project.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.moocafe.project.entity.InventoryStore;
import com.moocafe.project.repository.InventoryStoreRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

@Repository
@Transactional
@RequiredArgsConstructor
public class OutBoundInventoryStoreDao {
    private final InventoryStoreRepository inventoryStoreRepository;
    @PersistenceContext
    private EntityManager entityManager;
    public void save(InventoryStore inventoryStore){
        inventoryStoreRepository.save(inventoryStore);
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void decreaseStock(String itemCode, Integer qty){
        inventoryStoreRepository.decreaseStock(1, itemCode, qty);
    }
}
