package com.moocafe.project.repository;

import com.moocafe.project.dto.OutBoundListResponseDto;
import com.moocafe.project.entity.OutBound;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutBoundRepository extends JpaRepository<OutBound, Integer> {
    @Query(
            value = "SELECT " +
                    "o.OutBoundId, s.name AS storeName, i.itemCode, i.itemName, oi.ReceivedQuantity, " +
                    "TO_CHAR(o.DueDate, 'YYYY-MM-DD') AS dueDate, o.status " +
                    "FROM OutBound o " +
                    "JOIN Store s ON o.storeId = s.id " +
                    "JOIN OutBoundItem oi ON o.OutBoundId = oi.OutBoundId " +
                    "JOIN InventoryItem i ON oi.ItemCode = i.ItemCode " +
                    "WHERE TO_CHAR(o.RequiredDate, 'YYYY-MM-DD') BETWEEN :startDate AND :endDate " +
                    "AND (:storeName IS NULL OR s.name LIKE '%' || :storeName || '%')",
            nativeQuery = true)
    List<Object[]> findOutBoundsByConditionNative(
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("storeName") String storeName
    );

}
