package com.moocafe.project.service;

import com.moocafe.project.dao.ItemSearchDao;
import com.moocafe.project.dao.PurchaseDao;
import com.moocafe.project.dao.PurchaseItemDao;
import com.moocafe.project.dto.InventoryItemDto;
import com.moocafe.project.dto.ItemSearchDto;
import com.moocafe.project.dto.PurchaseDto;
import com.moocafe.project.dto.PurchaseItemDto;
import com.moocafe.project.entity.InventoryItem;
import com.moocafe.project.entity.InventoryStore;
import com.moocafe.project.entity.Purchase;
import com.moocafe.project.entity.PurchaseItem;
import com.moocafe.project.repository.InventoryItemRepository;
import com.moocafe.project.repository.InventoryStoreRepository;
import com.moocafe.project.repository.PurchaseItemRepository;
import com.moocafe.project.repository.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseDao purchaseDao;
    private final PurchaseItemDao purchaseItemDao;
    private final PurchaseRepository purchaseRepository;
    private final PurchaseItemRepository purchaseItemRepository;
    private final InventoryItemRepository inventoryItemRepository;
    private final InventoryStoreRepository inventoryStoreRepository;
    private final ItemSearchDao itemSearchDao;

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


    public void updateInventoryByPurchase(String purchaseNumber) {
        purchaseDao.updateInventoryByPurchase(purchaseNumber);
    }


    @Transactional
    public List<ItemSearchDto> getAllItems() {
        return inventoryItemRepository.findAll().stream()
                .map(item -> {
                    String code = item.getItemCode().trim();  // 공백 방지
                    int stockCount = inventoryStoreRepository.findByStoreIdAndItemCode(1, code)
                            .map(InventoryStore::getCount)
                            .orElse(0);  // 없으면 0

                    return ItemSearchDto.builder()
                            .itemCode(item.getItemCode())
                            .itemName(item.getItemName())
                            .itemPrice(item.getItemPrice())
                            .stockQuantity(stockCount)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public List<ItemSearchDto> searchByName(String name) {
        return itemSearchDao.searchItems(name).stream()
                .map(item -> ItemSearchDto.builder()
                        .itemCode(item.getItemCode())
                        .itemName(item.getItemName())
                        .itemPrice(item.getItemPrice())
                        .stockQuantity(item.getItemQuantity()) // 또는 item.getCount()
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public List<ItemSearchDto> searchByCode(String code) {
        return itemSearchDao.searchCode(code).stream()
                .map(item -> ItemSearchDto.builder()
                        .itemCode(item.getItemCode())
                        .itemName(item.getItemName())
                        .itemPrice(item.getItemPrice())
                        .stockQuantity(item.getItemQuantity()) // 또는 item.getCount()
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public List<PurchaseItemDto> findItemsByPurchaseNumber(String purchaseNumber) {
        Optional<Purchase> optionalPurchase = purchaseRepository.findByPurchaseNumber(purchaseNumber);

        if (optionalPurchase.isPresent()) {
            Purchase purchase = optionalPurchase.get();

            // 예: PurchaseItemDto 리스트 만들기
            List<PurchaseItemDto> itemDtos = purchaseItemDao.findItemByPurchaseNumber(purchase)
                    .stream()
                    .map(item -> PurchaseItemDto.builder()
                            .purchaseNumber(item.getPurchaseNumber().getPurchaseNumber())
                            .itemCode(item.getItemCode().getItemCode())
                            .itemName(item.getItemCode().getItemName())
                            .supplier(item.getSupplier())
                            .receivedQuantity(item.getReceivedQuantity())
                            .dueDate(item.getDueDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                            .expirationDate(item.getExpirationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                            .status(item.getStatus())
                            .build()
                    ).collect(Collectors.toList());

            return itemDtos;
        } else {
            // 없는 경우 예외처리
            throw new RuntimeException("해당 발주번호에 대한 정보가 없습니다: " + purchaseNumber);
        }
    }

    @Transactional
    public void updateStatusToComplete(Purchase purchaseNumber, InventoryItem itemCode) {
        PurchaseItem item = purchaseItemRepository
                .findByPurchaseNumberAndItemCode(purchaseNumber, itemCode)
                .orElseThrow(() -> new RuntimeException("해당 발주 품목이 없습니다"));
        item.setStatus("완료");
    }
}
