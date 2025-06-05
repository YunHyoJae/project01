package com.moocafe.project.service;

import com.moocafe.project.dao.InventoryStoreDao;
import com.moocafe.project.dao.PurchaseDao;
import com.moocafe.project.dto.PurchaseItemDto;
import com.moocafe.project.entity.InventoryItem;
import com.moocafe.project.entity.Purchase;
import com.moocafe.project.entity.PurchaseItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchaseService {
    private final PurchaseDao purchaseDao;
    private final InventoryStoreDao inventoryStoreDao;

    public void savePurchase(PurchaseItemDto dto) {
        Purchase purchase = Purchase.builder()
                .purchaseNumber(dto.getPurchaseNumber())
                .orderedDate(LocalDateTime.now())
                .typeOrder("발주")
                .build();

        List<PurchaseItem> itemList = dto.getItems().stream().map(itemDto -> {
            InventoryItem item = (InventoryItem) inventoryStoreDao.getStoreInventorySummary();
            return PurchaseItem.builder()
                    .itemCode(item)
                    .supplier(itemDto.getSupplier())
                    .receivedQuantity(itemDto.getReceivedQuantity())
                    .dueDate(itemDto.getDueDate())
                    .expirationDate(itemDto.getExpirationDate())
                    .status("진행중")
                    .purchaseNumber(purchase)
                    .build();
        }).collect(Collectors.toList()).reversed();

        purchaseDao.savePurchase(purchase);
    }

}
