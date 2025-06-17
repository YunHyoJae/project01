package com.moocafe.project.service;

import com.moocafe.project.dao.InventoryItemDao;
import com.moocafe.project.dto.InventoryItemDto;
import com.moocafe.project.entity.InventoryItem;
import com.moocafe.project.repository.InventoryItemRepository;
import lombok.Builder;
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
        boolean result = inventoryItemDao.insertItem(item) > 0;

        // DTO → Entity 변환 (builder 사용X, 생성자 직접 사용)
        try {
            // expirationDate 타입 변환 필요 (String → java.util.Date)
            java.util.Date parsedDate = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(item.getExpirationDate());

            InventoryItem entity = new InventoryItem(
                    item.getItemCode(),
                    item.getItemName(),
                    item.getItemGroup(),
                    item.getItemStandard(),
                    item.getItemQuantity(),
                    item.getItemPrice(),
                    item.getItemClass(),
                    parsedDate
            );

            inventoryItemRepository.save(entity);

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("InventoryItem 저장 실패: " + ex.getMessage());
        }

        return result;
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
