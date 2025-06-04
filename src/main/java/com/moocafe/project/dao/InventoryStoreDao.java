package com.moocafe.project.dao;

import com.moocafe.project.dto.InventorySummaryDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class InventoryStoreDao {

    @PersistenceContext
    private EntityManager em;

    public List<InventorySummaryDto> getStoreInventorySummary() {
        String sql = """
                SELECT i.itemCode, r.itemName, i.storeId, SUM(NVL(i.count, 0)) AS totalCount
                FROM InventoryStore i
                JOIN InventoryRegistration r ON i.itemCode = r.itemCode
                GROUP BY i.itemCode, r.itemName, i.storeId
                ORDER BY i.itemCode, i.storeId
                """;

        return em.createNativeQuery(sql, "InventorySummaryMapping").getResultList();
    }
}