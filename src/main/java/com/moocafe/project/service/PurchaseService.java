package com.moocafe.project.service;

import com.moocafe.project.dao.PurchaseDao;
import com.moocafe.project.dao.PurchaseItemDao;
import com.moocafe.project.dto.PurchaseDto;
import com.moocafe.project.entity.InventoryItem;
import com.moocafe.project.entity.Purchase;
import com.moocafe.project.entity.PurchaseItem;
import com.moocafe.project.repository.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseDao purchaseDao;
    private final PurchaseItemDao purchaseItemDao;
    private final PurchaseRepository purchaseRepository;

    @Transactional
    public void savePurchase(PurchaseDto purchaseDto) {

        Purchase purchase = Purchase.builder()
                .purchaseNumber(purchaseDto.getPurchaseNumber())
                .orderedDate(LocalDateTime.now())
                .typeOrder("발주")
                .build();

        purchaseDao.savePurchase(purchase);

        purchaseDto.getItems().stream()
                .filter(itemDto -> itemDto.getItemCode() != null && !itemDto.getItemCode().isBlank())
                .forEach(itemDto -> {
                    // InventoryItem 객체 생성 (Entity 변경 없이 연관관계 연결용)
                    InventoryItem inventoryItem = new InventoryItem(
                            itemDto.getItemCode(), null, null, null, null, null, null, null
                    );

                    Date dueDate = null;
                    Date expirationDate = null;

                    if (itemDto.getDueDate() != null) {
                        dueDate = Date.from(itemDto.getDueDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
                    }

                    if (itemDto.getExpirationDate() != null) {
                        expirationDate = Date.from(itemDto.getExpirationDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
                    }

                    if (dueDate == null || expirationDate == null) {
                        return;
                    }

                    PurchaseItem item = PurchaseItem.builder()
                            .itemCode(inventoryItem)
                            .supplier(itemDto.getSupplier())
                            .receivedQuantity(itemDto.getReceivedQuantity())
                            .dueDate(dueDate)
                            .expirationDate(expirationDate)
                            .status(itemDto.getStatus() != null ? itemDto.getStatus() : "진행중")
                            .purchaseNumber(purchase)
                            .build();

                    purchaseItemDao.savePurchaseItem(item);
                });
    }

    @Transactional(readOnly = true)
    public List<Purchase> findAll() {
        return purchaseRepository.findAll();
    }
}
