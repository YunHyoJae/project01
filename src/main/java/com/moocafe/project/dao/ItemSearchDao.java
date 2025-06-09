package com.moocafe.project.dao;

import com.moocafe.project.entity.InventoryItem;
import com.moocafe.project.repository.ItemSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemSearchDao {
    private final ItemSearchRepository itemSearchRepository;


    public List<InventoryItem> searchItems(String keyword) {
        return itemSearchRepository.findByItemNameContainingIgnoreCase(keyword);
    }

    public List<InventoryItem> searchCode(String code) {
        return itemSearchRepository.findByItemCodeContainingIgnoreCase(code);
    }

    public List<InventoryItem> getAllItems() {
        return itemSearchRepository.findAll();
    }
}

