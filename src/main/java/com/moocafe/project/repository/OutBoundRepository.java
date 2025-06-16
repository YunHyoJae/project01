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

    @Query(value = "SELECT o.outBoundId, s.name AS storeName, oi.itemCode, i.itemName, " +
            "oi.receivedQuantity, TO_CHAR(o.requiredDate, 'YYYY-MM-DD') AS requiredDate, " +
            "TO_CHAR(o.dueDate, 'YYYY-MM-DD') AS dueDate, o.status " +
            "FROM OutBound o " +
            "JOIN store s ON o.storeId = s.id " +
            "JOIN OutBoundItem oi ON o.outBoundId = oi.outBoundId " +
            "JOIN inventoryRegistration i ON oi.itemCode = i.itemCode " +
            "WHERE o.storeId != 1", nativeQuery = true)
    List<Object[]> findAllOutBoundDtosNative();

    @Query(
            value = "SELECT " +
                    "o.outBoundId, s.name AS storeName, i.itemCode, i.itemName, oi.receivedQuantity, " +
                    "TO_CHAR(o.requiredDate, 'YYYY-MM-DD') AS requiredDate, " +
                    "TO_CHAR(o.dueDate, 'YYYY-MM-DD') AS dueDate, o.status " +
                    "FROM OutBound o " +
                    "JOIN Store s ON o.storeId = s.id " +
                    "JOIN OutBoundItem oi ON o.outBoundId = oi.outBoundId " +
                    "JOIN inventoryRegistration i ON oi.itemCode = i.itemCode " +
                    "WHERE o.storeId != 1 " +
                    "AND TO_CHAR(o.requiredDate, 'YYYY-MM-DD') BETWEEN :startDate AND :endDate " +
                    "AND (:storeName IS NULL OR s.name LIKE '%' || :storeName || '%')",
            nativeQuery = true)
    List<Object[]> findOutBoundsByConditionNative(
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("storeName") String storeName
    );

    @Query(value = """
    SELECT o.outBoundId, s.name AS store_name, i.itemCode, i.itemName, oi.receivedQuantity,
           TO_CHAR(o.requiredDate, 'YYYY-MM-DD') AS requiredDate,
           TO_CHAR(o.dueDate, 'YYYY-MM-DD') AS dueDate, o.status
      FROM OUTBOUNDITEM oi
      JOIN OUTBOUND o ON oi.outBoundId = o.outBoundId
      JOIN STORE s ON o.storeId = s.id
      JOIN INVENTORYREGISTRATION i ON oi.itemCode = i.itemCode
     ORDER BY o.requiredDate DESC, o.outBoundId DESC
     FETCH FIRST 4 ROWS ONLY
    """, nativeQuery = true)
    List<Object[]> findRecentOutBoundListRaw();
}
