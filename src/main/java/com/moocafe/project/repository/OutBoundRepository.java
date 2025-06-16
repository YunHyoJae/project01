package com.moocafe.project.repository;

import com.moocafe.project.entity.OutBound;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface OutBoundRepository extends JpaRepository<OutBound, Integer> {

    @Query(
            value = "SELECT o.outBoundId, s.name AS storeName, i.itemCode, i.itemName, oi.receivedQuantity, " +
                    "TO_CHAR(o.requiredDate, 'YYYY-MM-DD') AS requiredDate, " +
                    "TO_CHAR(o.dueDate, 'YYYY-MM-DD') AS dueDate, o.status " +
                    "FROM OutBound o " +
                    "JOIN Store s ON o.storeId = s.id " +
                    "JOIN OutBoundItem oi ON o.outBoundId = oi.outBoundId " +
                    "JOIN inventoryRegistration i ON oi.itemCode = i.itemCode " +
                    "WHERE o.storeId != 1 " +
                    "AND TO_CHAR(o.requiredDate, 'YYYY-MM-DD') BETWEEN :startDate AND :endDate " +
                    "AND (:storeName IS NULL OR s.name LIKE '%' || :storeName || '%')",
            countQuery = "SELECT COUNT(*) " +
                    "FROM OutBound o " +
                    "JOIN Store s ON o.storeId = s.id " +
                    "JOIN OutBoundItem oi ON o.outBoundId = oi.outBoundId " +
                    "JOIN inventoryRegistration i ON oi.itemCode = i.itemCode " +
                    "WHERE o.storeId != 1 " +
                    "AND TO_CHAR(o.requiredDate, 'YYYY-MM-DD') BETWEEN :startDate AND :endDate " +
                    "AND (:storeName IS NULL OR s.name LIKE '%' || :storeName || '%')",
            nativeQuery = true)
    Page<Object[]> findOutBoundsByConditionNativePageable(
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("storeName") String storeName,
            Pageable pageable
    );
}

