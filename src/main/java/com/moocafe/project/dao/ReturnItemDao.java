package com.moocafe.project.dao;

import com.moocafe.project.entity.ReturnItem;
import com.moocafe.project.repository.ReturnItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReturnItemDao {
    private final ReturnItemRepository returnItemRepository;

    public void saveReturnItem(ReturnItem returnItem) {
         returnItemRepository.save(returnItem);
    }

    // ReturnItemDao
    public Optional<ReturnItem> findByReturnNumberAndItemCode(String returnNumber, String itemCode) {
        return returnItemRepository.findByReturnEntity_ReturnNumberAndItem_ItemCode(returnNumber, itemCode);
    }

}
