package com.moocafe.project.service;

import com.moocafe.project.dao.PurchaseDao;
import com.moocafe.project.dao.PurchaseItemDao;
import com.moocafe.project.dto.PurchaseDto;
import com.moocafe.project.dto.PurchaseItemDto;
import com.moocafe.project.entity.InventoryItem;
import com.moocafe.project.entity.Purchase;
import com.moocafe.project.entity.PurchaseItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseDao purchaseDao;
    private final PurchaseItemDao purchaseItemDao;

    @Transactional
    public void savePurchase(PurchaseDto purchaseDto) {

        // 1. Purchase 저장
        Purchase purchase = Purchase.builder()
                .purchaseNumber(purchaseDto.getPurchaseNumber())
                .orderedDate(LocalDateTime.now())
                .typeOrder("발주")
                .build();

        purchaseDao.savePurchase(purchase);

        // 2. 각 PurchaseItem 저장
        purchaseDto.getItems().stream()
                .filter(itemDto -> itemDto.getItemCode() != null && !itemDto.getItemCode().isBlank())
                .forEach(itemDto -> {
                    // InventoryItem 객체 생성 (Entity 변경 없이 연관관계 연결용)
                    InventoryItem inventoryItem = new InventoryItem(
                            itemDto.getItemCode(), null, null, null, null, null, null, null
                    );

                    // LocalDate → Date 변환
                    Date dueDate = Date.from(itemDto.getDueDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
                    Date expirationDate = Date.from(itemDto.getExpirationDate().atStartOfDay(ZoneId.systemDefault()).toInstant());

                    PurchaseItem item = PurchaseItem.builder()
                            .itemCode(inventoryItem)
                            .supplier(itemDto.getSupplier())
                            .receivedQuantity(itemDto.getReceivedQuantity())
                            .dueDate(dueDate)
                            .expirationDate(expirationDate)
                            .status(itemDto.getStatus() != null ? itemDto.getStatus() : "진행중")
                            .purchaseNumber(purchase)
                            .build();

                    purchaseItemDao.savePurchaseItem(item);  // 단일 저장
                });
    }
}
