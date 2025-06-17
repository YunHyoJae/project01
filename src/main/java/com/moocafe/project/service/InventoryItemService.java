package com.moocafe.project.service;

import com.moocafe.project.dao.InventoryItemDao;
import com.moocafe.project.dto.InventoryItemDto;
import com.moocafe.project.entity.InventoryItem;
import com.moocafe.project.repository.InventoryItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

    // 추가: 모든 아이템 반환
    public List<InventoryItem> findAll() {
        return inventoryItemRepository.findAll();
    }

    // 추가: 아이템코드로 아이템명 반환
    public String findItemNameByItemCode(String itemCode) {
        return inventoryItemRepository.findByItemCode(itemCode)
                .map(InventoryItem::getItemName)
                .orElse("");
    }

    public boolean isItemCodeExists(String itemCode) {
        return inventoryItemRepository.existsByItemCode(itemCode);
    }
}
