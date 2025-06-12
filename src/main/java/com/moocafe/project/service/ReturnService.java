package com.moocafe.project.service;

import com.moocafe.project.dao.InventoryItemDao;
import com.moocafe.project.dao.ReturnDao;
import com.moocafe.project.dao.ReturnItemDao;
import com.moocafe.project.dto.ReturnDto;
import com.moocafe.project.dto.ReturnItemDto;
import com.moocafe.project.entity.InventoryStore;
import com.moocafe.project.entity.Return;
import com.moocafe.project.entity.ReturnItem;
import com.moocafe.project.repository.InventoryStoreRepository;
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

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void saveReturn(ReturnDto returnDto) {
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


            InventoryStore item = itemList.get(0);

            ReturnItem returnItem = ReturnItem.builder()
                    .itemCode(item)
                    .returnQuantity(dto.getReturnQuantity())
                    .status("진행중")
                    .returnEntity(returnEntity)
                    .build();

            returnItemDao.saveReturnItem(returnItem);
        }
    }
}
