package com.moocafe.project.service;

import com.moocafe.project.dao.InventoryItemDao;
import com.moocafe.project.dto.InventoryItemDto;
import com.moocafe.project.repository.InventoryItemRepository;
import org.springframework.stereotype.Service;

@Service
public class InventoryItemService {

    private final InventoryItemRepository inventoryItemRepository;
    private final InventoryItemDao inventoryItemDao;

    public InventoryItemService(InventoryItemDao inventoryItemDao,
                                InventoryItemRepository inventoryItemRepository) {
        this.inventoryItemDao = inventoryItemDao;
        this.inventoryItemRepository = inventoryItemRepository;
    }

    public boolean registerItem(InventoryItemDto item) {
        System.out.println("▶ insert 시도: " + item.getItemCode());
        return inventoryItemDao.insertItem(item) > 0;
    }
}
