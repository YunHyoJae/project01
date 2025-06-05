package com.moocafe.project.dao;

import com.moocafe.project.entity.ReturnItem;
import com.moocafe.project.repository.ReturnItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReturnItemDao {
    private final ReturnItemRepository repo;

    public ReturnItem save(ReturnItem returnItem) {
        return repo.save(returnItem);
    }
}
