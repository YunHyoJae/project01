package com.moocafe.project.service;

import com.moocafe.project.dao.ReturnDao;
import com.moocafe.project.dao.ReturnItemDao;
import com.moocafe.project.dto.ReturnDto;
import com.moocafe.project.dto.ReturnItemDto;
import com.moocafe.project.entity.InventoryStore;
import com.moocafe.project.entity.Return;
import com.moocafe.project.entity.ReturnItem;
import com.moocafe.project.entity.StoreOrder;
import com.moocafe.project.repository.InventoryStoreRepository;
import com.moocafe.project.repository.StoreOrderRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReturnService {

    private final ReturnDao returnDao;
    private final ReturnItemDao returnItemDao;
    private final InventoryStoreRepository inventoryStoreRepository;

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void saveReturn(ReturnDto dto) {
        // EntityManager로 StoreOrder 직접 조회
        StoreOrder storeOrder = em.createQuery(
                        "SELECT s FROM StoreOrder s WHERE s.OrderNumber = :orderNumber", StoreOrder.class)
                .setParameter("orderNumber", dto.getOrderNumber())
                .getSingleResult();

        // Return 생성 및 저장
        Return returnEntity = Return.builder()
                .returnNumber(dto.getReturnNumber())
                .returnNote(dto.getReturnNote())
                .orderNumber(storeOrder)
                .build();

        Return savedReturn = returnDao.saveReturn(returnEntity);

        // ReturnItem 각각 저장
        for (ReturnItemDto itemDto : dto.getItems()) {
            InventoryStore inventoryStore = (InventoryStore) inventoryStoreRepository.findByItemCode(itemDto.getItemCode());

            ReturnItem returnItem = ReturnItem.builder()
                    .returnEntity(savedReturn)
                    .itemCode(inventoryStore)
                    .returnQuantity(itemDto.getReturnQuantity())
                    .build();

            returnItemDao.saveReturnItem(returnItem);
        }
    }
}

