package com.moocafe.project.repository;

import com.moocafe.project.dto.StoreOrderListResponseDto;
import com.moocafe.project.entity.Return;
import com.moocafe.project.entity.StoreOrderDetail;
import com.moocafe.project.entity.StoreOrderDetailId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreOrderDetailRepository extends JpaRepository<StoreOrderDetail, StoreOrderDetailId> {
//    @Query(value = """
//        SELECT o.orderNumber, TO_CHAR(o.orderDate, 'YYYY-MM-DD'), d.itemCode,
//               ir.itemName, d.orderedQuantity, d.status
//        FROM StoreOrder o
//        JOIN StoreOrderDetail d ON o.id = d.orderId
//        JOIN InventoryStore s ON d.itemCode = s.itemCode AND s.storeId = 1
//        JOIN InventoryRegistration ir ON s.itemCode = ir.itemCode
//        WHERE o.storeId = :storeId
//        AND TRUNC(o.orderDate) BETWEEN TO_DATE(:startDate, 'YYYY-MM-DD') AND TO_DATE(:endDate, 'YYYY-MM-DD')
//        """, nativeQuery = true)
//    List<Object[]> findOrderListByStoreAndDate(
//            @Param("storeId") Integer storeId,
//            @Param("startDate") String startDate,
//            @Param("endDate") String endDate
//    );
//강동현 수정 부분
@Query(value = """
    SELECT o.orderNumber,
           TO_CHAR(o.orderDate, 'YYYY-MM-DD'),
           d.itemCode,
           ir.itemName,
           d.orderedQuantity,
           d.status,
           ri.status AS returnStatus
    FROM StoreOrder o
    JOIN StoreOrderDetail d ON o.id = d.orderId
    JOIN InventoryStore s ON d.itemCode = s.itemCode AND s.storeId = 1
    JOIN InventoryRegistration ir ON s.itemCode = ir.itemCode
    LEFT JOIN ReturnItem ri ON ri.returnNumber = o.orderNumber AND ri.itemCode = d.itemCode
    WHERE o.storeId = :storeId
    AND TRUNC(o.orderDate) BETWEEN TO_DATE(:startDate, 'YYYY-MM-DD') AND TO_DATE(:endDate, 'YYYY-MM-DD')
    """, nativeQuery = true)
List<Object[]> findOrderListByStoreAndDate(
        @Param("storeId") Integer storeId,
        @Param("startDate") String startDate,
        @Param("endDate") String endDate
);


//강동현 수정 부분



    @Query("""
    SELECT d FROM StoreOrderDetail d
    JOIN d.storeOrder o
    WHERE o.storeId = :storeId
    AND d.itemCode = :itemCode
    AND d.orderedQuantity = :quantity
    ORDER BY o.orderDate ASC
""")
    List<StoreOrderDetail> findSpecificOrderForOutBound(
            @Param("storeId") Integer storeId,
            @Param("itemCode") String itemCode,
            @Param("quantity") int quantity,
            @Param("currentStatus") String currentStatus
    );

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("""
        UPDATE StoreOrderDetail d SET d.status = :status
        WHERE d.orderId = :orderId AND d.itemCode = :itemCode
    """)
    void updateStatusByOrderIdAndItemCode(
            @Param("orderId") Long orderId,
            @Param("itemCode") String itemCode,
            @Param("status") String status
    );


    Optional<StoreOrderDetail> findByStoreOrder_idAndItemCode(Long id, String itemCode);


}
