package com.moocafe.project.repository;

import com.moocafe.project.entity.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemSearchRepository extends JpaRepository<InventoryItem, Integer> {
    List<InventoryItem> findByItemNameContainingIgnoreCase(String keyword);

    List<InventoryItem> findByItemCodeContainingIgnoreCase(String code);

}
