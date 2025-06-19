package com.moocafe.project.service;

import com.moocafe.project.dao.*;
import com.moocafe.project.dto.ReturnDto;
import com.moocafe.project.dto.ReturnItemDto;
import com.moocafe.project.entity.*;
import com.moocafe.project.repository.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReturnService {

    private final ReturnDao returnDao;
    private final ReturnItemDao returnItemDao;
    private final InventoryStoreRepository inventoryStoreRepository;
    private final ReturnRepository returnRepository;
    private final InventoryItemRepository inventoryItemRepository;
    private final StoreOrderDao storeOrderDao;
    //@PersistenceContext
    //private EntityManager em;

    @Transactional
    public void saveReturn(ReturnDto returnDto, int storeId) {
        Return returnEntity = Return.builder()
                .returnNumber(returnDto.getReturnNumber())
                .returnNote(returnDto.getReturnNote())
                .requiredDate(LocalDateTime.now())
                .typeReturn("반품")
                .build();

        returnDao.saveReturn(returnEntity);

        for (ReturnItemDto dto : returnDto.getItems()) {
            List<InventoryStore> itemList = inventoryStoreRepository.findByItemCode(dto.getItemCode());

            if (itemList.isEmpty()) {
                throw new RuntimeException("등록되지 않은 품목코드: " + dto.getItemCode());
            }

            InventoryItem invItem = inventoryItemRepository.findByItemCode(dto.getItemCode())
                    .orElseThrow(() -> new RuntimeException("존재하지 않는 품목코드: " + dto.getItemCode()));

            ReturnItem returnItem = ReturnItem.builder()
                    .item(invItem)
                    .returnQuantity(dto.getReturnQuantity())
                    .status("진행중")
                    .returnEntity(returnEntity)
                    .build();

            returnItemDao.saveReturnItem(returnItem);

            // 여기서도 dto에서 값 가져와야 합니다.
            inventoryStoreRepository.increaseHeadOfficeStock(dto.getItemCode(), dto.getReturnQuantity());
            inventoryStoreRepository.decreaseStoreStock(dto.getItemCode(), dto.getReturnQuantity(), storeId);
        }
    }

    public StoreOrder findByOrderNumber(String orderNumber) {
        return storeOrderDao.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 주문번호: " + orderNumber));
    }

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


}
