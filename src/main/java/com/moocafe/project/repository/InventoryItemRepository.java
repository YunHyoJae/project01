package com.moocafe.project.repository;

import com.moocafe.project.entity.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, String> {

    Optional<InventoryItem> findByItemCode(String itemCode);

    boolean existsByItemCode(String itemCode);
}