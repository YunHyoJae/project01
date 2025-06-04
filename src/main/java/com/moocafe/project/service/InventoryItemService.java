package com.moocafe.project.service;

import com.moocafe.project.dao.InventoryItemDao;
import com.moocafe.project.dto.InventoryItemDto;
import com.moocafe.project.entity.InventoryItem;
import com.moocafe.project.repository.InventoryItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryItemService {
    @Autowired
    private InventoryItemDao inventoryItemDao;
    private InventoryItemRepository inventoryItemRepository;

    public boolean registerItem(InventoryItemDto item) {
        return inventoryItemDao.insertItem(item) > 0;
    }

    public void register(InventoryItem item) {
        if (inventoryItemRepository.existsByItemCode(item.getItemCode())) {
            throw new IllegalArgumentException("중복된 품목코드입니다.");
        }
        inventoryItemRepository.save(item);
    }
}
