package com.moocafe.project.dao;

import com.moocafe.project.dto.StoreOrderListResponseDto;
import com.moocafe.project.entity.StoreOrderDetail;
import com.moocafe.project.entity.StoreOrderDetailId;
import com.moocafe.project.repository.StoreOrderDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StoreOrderDetailDao {

    private final StoreOrderDetailRepository storeOrderDetailRepository;

    public StoreOrderDetail save(StoreOrderDetail detail) {
        return storeOrderDetailRepository.save(detail);
    }

    public Optional<StoreOrderDetail> findById(StoreOrderDetailId id) {
        return storeOrderDetailRepository.findById(id);
    }

    public List<StoreOrderDetail> findAll() {
        return storeOrderDetailRepository.findAll();
    }

    public void delete(StoreOrderDetail detail) {
        storeOrderDetailRepository.delete(detail);
    }

    public List<Object[]> findOrderListByStoreAndDate(Integer storeId, String startDate, String endDate) {
        return storeOrderDetailRepository.findOrderListByStoreAndDate(storeId, startDate, endDate);
    }
}
