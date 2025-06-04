package com.moocafe.project.service;

import com.moocafe.project.entity.InventoryStore;
import com.moocafe.project.repository.InventoryStoreRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class InventoryStoreService {

    private final InventoryStoreRepository repository;

    public InventoryStoreService(InventoryStoreRepository repository) {
        this.repository = repository;
    }

    public List<InventoryStore> getInventoryByStore(Integer storeId) {
        return repository.findByStoreId(storeId);
    }

    public void insertInventory(String itemCode, Integer storeId, Integer count) {
        InventoryStore store = new InventoryStore(itemCode, storeId, count, new Date());
        repository.save(store);
    }

    public void updateCount(Long id, Integer newCount) {
        InventoryStore store = repository.findById(id).orElseThrow();
        store.updateCount(newCount, new Date());
        repository.save(store);
    }
}
