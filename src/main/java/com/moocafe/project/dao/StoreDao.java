package com.moocafe.project.dao;

import com.moocafe.project.entity.Store;
import com.moocafe.project.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StoreDao {
    private final StoreRepository storeRepository;
    public Store save(Store store) {
        return storeRepository.save(store);
    }
}
