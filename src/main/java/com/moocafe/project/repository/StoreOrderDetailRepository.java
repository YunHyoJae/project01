package com.moocafe.project.repository;

import com.moocafe.project.dto.StoreOrderListResponseDto;
import com.moocafe.project.entity.StoreOrderDetail;
import com.moocafe.project.entity.StoreOrderDetailId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreOrderDetailRepository extends JpaRepository<StoreOrderDetail, StoreOrderDetailId> {
    @Query(
            value = "SELECT o.OrderNumber, TO_CHAR(o.OrderDate, 'YYYY-MM-DD'), d.ItemCode, i.ItemName, d.OrderedQuantity, d.status " +
                    "FROM StoreOrder o " +
                    "JOIN StoreOrderDetail d ON o.id = d.orderId " +
                    "JOIN InventoryRegistration i ON d.ItemCode = i.ItemCode " +
                    "WHERE o.storeId = :storeId " +
                    "AND o.OrderDate BETWEEN TO_DATE(:startDate, 'YYYY-MM-DD') AND TO_DATE(:endDate, 'YYYY-MM-DD')",
            nativeQuery = true
    )
    List<Object[]> findOrderListByStoreAndDate(
            @Param("storeId") Integer storeId,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate
    );
}
