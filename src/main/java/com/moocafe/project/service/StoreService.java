package com.moocafe.project.service;

import com.moocafe.project.entity.Store;
import com.moocafe.project.repository.StoreRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StoreService {

    private final StoreRepository storeRepository;

    public StoreService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    // 전체 매장 조회
    public List<Store> findAll() {
        return storeRepository.findAll();
    }

    // ID로 매장 조회
    public Optional<Store> findById(Integer id) {
        return storeRepository.findById(id);
    }

    // 매장명으로 검색
    public List<Store> findByNameContaining(String name) {
        return storeRepository.findByNameContaining(name);
    }

    // 매장 저장
    public Store save(Store store) {
        return storeRepository.save(store);
    }

    // 매장 삭제
    public void deleteById(Integer id) {
        storeRepository.deleteById(id);
    }
}