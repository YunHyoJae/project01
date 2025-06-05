package com.moocafe.project.dao;

import com.moocafe.project.entity.StoreOrder;
import com.moocafe.project.repository.StoreOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StoreOrderDao {

    private final StoreOrderRepository storeOrderRepository;

    public StoreOrder save(StoreOrder storeOrder) {
        return storeOrderRepository.save(storeOrder);
    }

    public Optional<StoreOrder> findById(Integer id) {
        return storeOrderRepository.findById(id);
    }

    public List<StoreOrder> findAll() {
        return storeOrderRepository.findAll();
    }

    public void delete(StoreOrder storeOrder) {
        storeOrderRepository.delete(storeOrder);
    }
}
