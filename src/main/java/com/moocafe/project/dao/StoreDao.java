package com.moocafe.project.dao;

import com.moocafe.project.entity.Store;
import com.moocafe.project.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Optional<Store> findById(int id) {return storeRepository.findById(id);}
    public List<Store> findAll() {return storeRepository.findAll();}
    public Page<Store> findAllExcept(int id, Pageable pageable) {return storeRepository.findAllExcept(id, pageable);}
    public Page<Store> findByNameExcept(int id, String keyword, Pageable pageable) {return storeRepository.findByNameExcept(id, keyword, pageable);}
    public Page<Store> findByStateExcept(int id, String keyword, Pageable pageable) {return storeRepository.findByStateExcept(id, keyword, pageable);}
    public Page<Store> findBySpaceExcept(int id, String keyword, Pageable pageable) {return storeRepository.findBySpaceExcept(id, keyword, pageable);}
    public Page<Store> findByUserNameExcept(int id, String keyword, Pageable pageable) {return storeRepository.findByUserNameExcept(id, keyword, pageable);}
    public Page<Store> findByUserTelExcept(int id, String keyword, Pageable pageable) {return storeRepository.findByUserTelExcept(id, keyword, pageable);}

}
