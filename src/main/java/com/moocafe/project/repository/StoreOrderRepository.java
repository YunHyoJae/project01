package com.moocafe.project.repository;

import com.moocafe.project.dto.StoreOrderListResponseDto;
import com.moocafe.project.entity.StoreOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
    public interface StoreOrderRepository extends JpaRepository<StoreOrder, Integer> {

        @Query(
                value = "SELECT COUNT(*) FROM StoreOrder " +
                        "WHERE storeId = :storeId AND TO_CHAR(OrderDate, 'YYYYMMDD') = :today",
                nativeQuery = true
        )
        int countByStoreIdAndDate(@Param("storeId") Integer storeId, @Param("today") String today);
        //Optional<StoreOrder> findById(Integer id);
    }