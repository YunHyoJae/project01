package com.moocafe.project.service;

import com.moocafe.project.dao.InventoryItemDao;
import com.moocafe.project.dao.PurchaseItemDao;
import com.moocafe.project.dao.ReturnDao;
import com.moocafe.project.dao.ReturnItemDao;
import com.moocafe.project.dto.ReturnDto;
import com.moocafe.project.dto.ReturnItemDto;
import com.moocafe.project.entity.*;
import com.moocafe.project.repository.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReturnService {

    private final ReturnDao returnDao;
    private final ReturnItemDao returnItemDao;
    private final InventoryStoreRepository inventoryStoreRepository;
    private final ReturnRepository returnRepository;
    private final InventoryItemRepository inventoryItemRepository;
    private final PurchaseRepository purchaseRepository;
    private final PurchaseItemRepository purchaseItemRepository;

    //@PersistenceContext
    //private EntityManager em;

    @Transactional
    public void saveReturn(ReturnDto returnDto, String itemCode, Integer returnQuantity, int storeId) {
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

            // dto.getItemCode()가 제품코드라면
            InventoryItem invItem = inventoryItemRepository.findByItemCode(dto.getItemCode())
                    .orElseThrow(() -> new RuntimeException("존재하지 않는 품목코드: " + dto.getItemCode()));


            ReturnItem returnItem = ReturnItem.builder()
                    .item(invItem)
                    .returnQuantity(dto.getReturnQuantity())
                    .status("진행중")
                    .returnEntity(returnEntity)
                    .build();

            returnItemDao.saveReturnItem(returnItem);
//            returnDao.updateReturnStock(itemCode, returnQuantity, storeId);

            inventoryStoreRepository.increaseHeadOfficeStock(itemCode, returnQuantity);
            inventoryStoreRepository.decreaseStoreStock(itemCode, returnQuantity, storeId);
        }
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


}
