package com.moocafe.project.repository;

import com.moocafe.project.entity.InventoryStore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryStoreRepository extends JpaRepository<InventoryStore, Long> {

    List<InventoryStore> findByStoreId(Integer storeId);

    List<InventoryStore> findByItemCode(String itemCode);

    List<InventoryStore> findByItemCodeAndStoreId(String itemCode, Integer storeId);
}
