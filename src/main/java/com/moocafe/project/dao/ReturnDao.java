package com.moocafe.project.dao;

import com.moocafe.project.entity.Return;
import com.moocafe.project.entity.ReturnItem;
import com.moocafe.project.repository.InventoryStoreRepository;
import com.moocafe.project.repository.ReturnItemRepository;
import com.moocafe.project.repository.ReturnRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReturnDao {
    private final ReturnRepository returnRepository;
    private final ReturnItemRepository returnItemRepository;
    private final InventoryStoreRepository inventoryStoreRepository;

    public Return saveReturn(Return returnEntity) {
        return returnRepository.save(returnEntity);
    }

    public void updateReturnStock(String itemCode, int returnQuantity, int storeId) {
        inventoryStoreRepository.increaseHeadOfficeStock(itemCode, returnQuantity);
        inventoryStoreRepository.decreaseStoreStock(itemCode, returnQuantity, storeId);
    }

    public List<ReturnItem> findByStoreId(int storeId) {
        return returnItemRepository.findByStoreId(storeId);
    }
}
