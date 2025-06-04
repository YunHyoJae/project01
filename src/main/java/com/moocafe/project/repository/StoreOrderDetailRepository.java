package com.moocafe.project.repository;

import com.moocafe.project.entity.StoreOrderDetail;
import com.moocafe.project.entity.StoreOrderDetailId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreOrderDetailRepository extends JpaRepository<StoreOrderDetail, StoreOrderDetailId> {
}
