package com.moocafe.project.repository;

import com.moocafe.project.dto.StoreOrderListResponseDto;
import com.moocafe.project.entity.StoreOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreOrderRepository extends JpaRepository<StoreOrder, Long> {
    @Query(
            value = "SELECT o.OrderNumber, TO_CHAR(o.OrderDate, 'YYYY-MM-DD'), d.ItemCode, i.ItemName, d.OrderedQuantity, d.status " +
                    "FROM StoreOrder o " +
                    "JOIN StoreOrderDetail d ON o.id = d.orderId " +
                    "JOIN InventoryRegistration i ON d.ItemCode = i.ItemCode",
            nativeQuery = true
    )
    Optional<StoreOrder> findById(Integer id);
}
