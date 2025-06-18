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

    public Optional<ReturnItem> findById(Integer id) {
        return returnItemRepository.findById(id);
    }

    public void save(ReturnItem item) {
        returnItemRepository.save(item);

    }
}
