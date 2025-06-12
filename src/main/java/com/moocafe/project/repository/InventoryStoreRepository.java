package com.moocafe.project.repository;

import com.moocafe.project.dto.InventorySummaryDto;
import com.moocafe.project.entity.InventoryStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface InventoryStoreRepository extends JpaRepository<InventoryStore, Long> {

    List<InventoryStore> findByStoreId(Integer storeId);

    List<InventoryStore> findByItemCode(String itemCode);

    List<InventoryStore> findByItemCodeAndStoreId(String itemCode, Integer storeId);

    /**
     * ✅ 수량 차감: 레시피 사용 시 재고 감소
     */
    @Modifying
    @Transactional
    @Query("""
        UPDATE InventoryStore i 
        SET i.count = i.count - :usedQty, 
            i.modifyDate = CURRENT_DATE 
        WHERE i.storeId = :storeId AND i.itemCode = :itemCode
    """)
    void decreaseStock(@Param("storeId") Integer storeId,
                       @Param("itemCode") String itemCode,
                       @Param("usedQty") int usedQty);

    /**
     * ✅ 전체 매장의 재고 요약 (itemCode + itemName + storeId별 수량 합계)
     */
    @Query(value = """
        SELECT 
            i.itemCode AS itemCode,
            r.itemName AS itemName,
            i.storeId AS storeId,
            SUM(i.count) AS totalCount
        FROM INVENTORYSTORE i
        JOIN INVENTORYREGISTRATION r ON i.itemCode = r.itemCode
        GROUP BY i.itemCode, r.itemName, i.storeId
        """, nativeQuery = true)
    List<InventorySummaryDto> getInventorySummary();

    /**
     * ✅ 특정 매장의 재고 요약
     */
    @Query(value = """
        SELECT 
            i.itemCode AS itemCode,
            r.itemName AS itemName,
            i.storeId AS storeId,
            SUM(i.count) AS totalCount
        FROM INVENTORYSTORE i
        JOIN INVENTORYREGISTRATION r ON i.itemCode = r.itemCode
        WHERE i.storeId = :storeId
        GROUP BY i.itemCode, r.itemName, i.storeId
        """, nativeQuery = true)
    List<InventorySummaryDto> getInventorySummaryByStoreId(@Param("storeId") Integer storeId);

    @Query("""
    SELECT COALESCE(i.count, 0)
    FROM InventoryStore i
    WHERE i.storeId = :storeId AND i.itemCode = :itemCode
""")
    Integer findQuantityByStoreIdAndItemCode(
            @Param("storeId") Integer storeId,
            @Param("itemCode") String itemCode
    );
}