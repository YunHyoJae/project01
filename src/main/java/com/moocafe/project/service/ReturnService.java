package com.moocafe.project.service;

import com.moocafe.project.dao.*;
import com.moocafe.project.dto.ReturnDto;
import com.moocafe.project.dto.ReturnItemDto;
import com.moocafe.project.dto.StoreOrderListResponseDto;
import com.moocafe.project.entity.*;
import com.moocafe.project.repository.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReturnService {

    private final ReturnDao returnDao;
    private final ReturnItemDao returnItemDao;
    private final InventoryStoreRepository inventoryStoreRepository;
    private final InventoryStoreDao inventoryStoreDao;
    private final ReturnRepository returnRepository;
    private final InventoryItemRepository inventoryItemRepository;
    private final StoreOrderDao storeOrderDao;
    private final StoreOrderDetailDao storeOrderDetailDao;
    private final ReturnItemRepository returnItemRepository;


    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void saveReturn(ReturnDto returnDto) {

        if (returnRepository.findByReturnNumber(returnDto.getReturnNumber()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 반품번호입니다: " + returnDto.getReturnNumber());
        }

        String orderNumber = returnDto.getItems().get(0).getOrderNumber();
        StoreOrder storeOrder = storeOrderDao.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new RuntimeException("해당 주문번호 없음"));

        // 1. Return 저장
        Return returnEntity = Return.builder()
                .returnNumber(returnDto.getReturnNumber())
                .returnNote(returnDto.getReturnNote())
                .requiredDate(LocalDateTime.now())
                .orderNumber(storeOrder)
                .typeReturn("반품")
                .build();

        returnDao.saveReturn(returnEntity);

        // 2. ReturnItem 저장 + 검증
        for (ReturnItemDto dto : returnDto.getItems()) {
            // List<InventoryStore> itemList = inventoryStoreRepository.findByItemCode(dto.getItemCode());

            // if (itemList.isEmpty()) {
            //     throw new RuntimeException("등록되지 않은 품목코드: " + dto.getItemCode());
            // }

            InventoryItem invItem = inventoryItemRepository.findByItemCode(dto.getItemCode())
                    .orElseThrow(() -> new RuntimeException("존재하지 않는 품목코드: " + dto.getItemCode()));

            Integer storeId = storeOrder.getStoreId();
            int currentStock = inventoryStoreRepository.findQuantityByStoreIdAndItemCode(storeId, dto.getItemCode());

            if (dto.getReturnQuantity() > currentStock) {
                throw new IllegalArgumentException("[" + dto.getItemCode() + "] 반품수량이 현재 재고(" + currentStock + ")보다 많습니다.");
            }

            ReturnItem returnItem = ReturnItem.builder()
                    .item(invItem)
                    .returnQuantity(dto.getReturnQuantity())
                    .status("진행중")
                    .returnEntity(returnEntity)
                    .build();

            returnItemDao.saveReturnItem(returnItem);
        }
    }


//    public StoreOrder findByOrderNumber(String orderNumber) {
//        return storeOrderDao.findByOrderNumber(orderNumber)
//                .orElseThrow(() -> new RuntimeException("존재하지 않는 주문번호: " + orderNumber));
//    }

    @Transactional(readOnly = true)
    public List<Return> findAllWithItems() {
        // Spring Data JPA의 findAll()은 items가 LAZY일 수 있으므로, fetch join이 필요하다면 커스텀 쿼리 사용 권장
        // 하지만 단순히 전체 리스트만 필요하다면 아래처럼 사용 가능
        List<Return> returns = returnRepository.findAll();
        // items 강제 초기화 (JPA LAZY 방지)
        returns.forEach(r -> r.getItems().size());
        return returns;
    }


//    public List<Return> findByStoreId(int storeId) {
//        return returnDao.findByStoreId(storeId);
//    }

    public List<ReturnDto> getReturnsByStoreId(Integer storeId) {
        List<Return> returns = returnDao.findByStoreIdWithItems(storeId);

        return returns.stream().map(r -> {
            List<ReturnItemDto> itemDtos = r.getItems().stream().map(item -> ReturnItemDto.builder()
                    .id(item.getId())
                    .itemCode(item.getItem().getItemCode())
                    .itemName(item.getItem().getItemName()) // ← 필요시 Join해서 가져오거나 DTO에서 name 처리
                    .returnQuantity(item.getReturnQuantity())
                    .status(item.getStatus())
                    .returnNumber(r.getReturnNumber())
                    .orderNumber(r.getOrderNumber().getOrderNumber())
                    .build()
            ).collect(Collectors.toList());

            return ReturnDto.builder()
                    .id(r.getId())
                    .returnNumber(r.getReturnNumber())
                    .requiredDate(r.getRequiredDate())
                    .returnNote(r.getReturnNote())
                    .typeReturn(r.getTypeReturn())
                    .orderNumber(r.getOrderNumber().getOrderNumber())
                    .storeId(r.getOrderNumber().getStoreId().toString()) // storeId가 객체일 경우
                    .items(itemDtos)
                    .build();
        }).collect(Collectors.toList());
    }

    @Transactional
    public void processReturn(String returnNumber) {
        // 1. 해당 반품 조회
        Return returnEntity = returnRepository.findByReturnNumber(returnNumber)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 반품번호: " + returnNumber));
        List<ReturnItem> items = returnEntity.getItems();
        if (items == null || items.isEmpty()) {
            throw new RuntimeException("반품 항목이 없습니다.");
        }

//        for (ReturnItem item : items) {
//            if (!"진행중".equals(item.getStatus())) {
//                continue; // 이미 처리된 항목은 skip
//            }
//
//            String itemCode = item.getItem().getItemCode();
//            int quantity = item.getReturnQuantity();
//
//            // 2. 매장 재고 감소
//            inventoryStoreDao.decreaseStoreStockByReturnNumber(returnNumber);
//
//            // 3. 본사 재고 증가
//            inventoryStoreDao.increaseHQStock(itemCode, quantity);
//
//            // 4. 상태 변경
//            item.setStatus("반품완료");
//            returnItemDao.saveReturnItem(item);
//        }
        //강동현 수정
        for (ReturnItem item : items) {
            if (!"진행중".equals(item.getStatus())) continue;

            String itemCode = item.getItem().getItemCode();
            int quantity = item.getReturnQuantity();

            Integer storeId = item.getReturnEntity()
                    .getOrderNumber()
                    .getStoreId();
            // ✔ 해당 itemCode, quantity로 재고 감소
            inventoryStoreDao.decreaseStoreStock(storeId, itemCode, quantity);

            inventoryStoreDao.increaseHQStock(itemCode, quantity);

            item.setStatus("반품완료");
            returnItemDao.saveReturnItem(item);
        }



        // 5. DB 반영 + 캐시 비움
        em.flush();
        em.clear();
    }

    @Transactional
    public void completeItem(String returnNumber, String itemCode) {
        // 1. ReturnItem 찾기
        ReturnItem item = returnItemDao.findByReturnNumberAndItemCode(returnNumber, itemCode)
                .orElseThrow(() -> new RuntimeException("해당 반품 항목이 없습니다."));

        // 이미 완료된 경우는 패스
        if ("반품완료".equals(item.getStatus())) return;

        int quantity = item.getReturnQuantity();

        Integer storeId = item.getReturnEntity()
                .getOrderNumber()
                .getStoreId();


        inventoryStoreDao.decreaseStoreStock(storeId, itemCode, quantity);

        inventoryStoreDao.increaseHQStock(itemCode, quantity);

        item.setStatus("반품완료");
        returnItemDao.saveReturnItem(item);

        Long id = item.getReturnEntity().getOrderNumber().getId();
        StoreOrderDetail detail = storeOrderDetailDao
                .findByStoreOrder_idAndItemCode(id, itemCode)
                .orElseThrow(() -> new RuntimeException("해당 주문 상세 항목이 없습니다."));

        detail.setStatus("반품완료");
        storeOrderDetailDao.save(detail);
    }
    @Transactional(readOnly = true)
    public List<ReturnDto> getReturnListByStoreId(Integer storeId) {
        List<Return> returns = returnDao.findByStoreIdWithItems(storeId);

        return returns.stream().map(r -> {
            List<ReturnItemDto> itemDtos = r.getItems().stream().map(item -> ReturnItemDto.builder()
                    .id(item.getId())
                    .itemCode(item.getItem().getItemCode())
                    .itemName(item.getItem().getItemName())
                    .returnQuantity(item.getReturnQuantity())
                    .status(item.getStatus())
                    .returnNumber(r.getReturnNumber())
                    .orderNumber(r.getOrderNumber().getOrderNumber())
                    .build()
            ).collect(Collectors.toList());

            return ReturnDto.builder()
                    .id(r.getId())
                    .returnNumber(r.getReturnNumber())
                    .requiredDate(r.getRequiredDate())
                    .returnNote(r.getReturnNote())
                    .typeReturn(r.getTypeReturn())
                    .orderNumber(r.getOrderNumber().getOrderNumber())
                    .storeId(r.getOrderNumber().getStoreId().toString())
                    .items(itemDtos)
                    .build();
        }).collect(Collectors.toList());
    }

    //강동현 추가//
    public boolean existsByReturnNumber(String returnNumber) {
        return returnRepository.findByReturnNumber(returnNumber).isPresent();
    }

    @Transactional(readOnly = true)
    public String generateUniqueReturnNumber() {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String returnNumber;
        do {
            String random = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
            returnNumber = "RE" + date + "-" + random;
        } while (returnRepository.findByReturnNumber(returnNumber).isPresent());
        return returnNumber;
    }

    public int getStockQuantity(Integer storeId, String itemCode) {
        return inventoryStoreRepository.findQuantityByStoreIdAndItemCode(storeId, itemCode);
    }

    public int getReturnQuantity(String returnNumber, String itemCode) {
        return returnItemRepository.findByReturnEntity_ReturnNumberAndItem_ItemCode(returnNumber, itemCode)
                .map(ReturnItem::getReturnQuantity)
                .orElse(0);
    }

    @Transactional(readOnly = true)
    public String getReturnStatus(String orderNumber, String itemCode) {
        return returnItemRepository
                .findByReturnEntity_ReturnNumberAndItem_ItemCode(orderNumber, itemCode)
                .map(ReturnItem::getStatus)
                .orElse(null);
    }





}