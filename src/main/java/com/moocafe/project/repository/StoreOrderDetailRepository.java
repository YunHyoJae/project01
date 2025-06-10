package com.moocafe.project.repository;

import com.moocafe.project.dto.StoreOrderListResponseDto;
import com.moocafe.project.entity.StoreOrderDetail;
import com.moocafe.project.entity.StoreOrderDetailId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreOrderDetailRepository extends JpaRepository<StoreOrderDetail, StoreOrderDetailId> {
    @Query(
            value = "SELECT o.orderNumber, TO_CHAR(o.orderDate, 'YYYY-MM-DD'), d.itemCode, i.itemName, d.orderedQuantity, d.status " +
                    "FROM StoreOrder o " +
                    "JOIN StoreOrderDetail d ON o.id = d.orderId " +
                    "JOIN InventoryRegistration i ON d.itemCode = i.itemCode " +
                    "WHERE o.storeId = :storeId " +
                    "AND TRUNC(o.orderDate) BETWEEN TO_DATE(:startDate, 'YYYY-MM-DD') AND TO_DATE(:endDate, 'YYYY-MM-DD')",
            nativeQuery = true
    )
    List<Object[]> findOrderListByStoreAndDate(
            @Param("storeId") Integer storeId,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate
    );
    @Modifying(clearAutomatically = true)
    @Query("UPDATE StoreOrderDetail d SET d.status = :status " +
            "WHERE d.itemCode = :itemCode AND d.storeOrder.storeId = :storeId")
    void updateStatusByStoreAndItem(@Param("storeId") Integer storeId,
                                    @Param("itemCode") String itemCode,
                                    @Param("status") String status);
}
