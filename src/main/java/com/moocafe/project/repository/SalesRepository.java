package com.moocafe.project.repository;

import com.moocafe.project.dto.SalesSummaryDto;
import com.moocafe.project.entity.Menu;
import com.moocafe.project.entity.Sales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface SalesRepository extends JpaRepository<Sales, Long> {
    List<Sales> findByStoreIdAndSaleTimeBetween(Integer storeId, Date start, Date end);
    @Query("SELECT m FROM Menu m JOIN MenuPrice p ON m.menuId = p.menuId WHERE m.menuId = :menuId")
    List<Menu> findByMenuIdWithPrice(@Param("menuId") String menuId);

    // 1. 날짜별 매출 (점주 페이지)
    @Query(value = """
        SELECT s.storeId AS storeId,
               st.name AS storeName,
               s.menuId AS menuId,
               s.menuName AS menuName,
               SUM(s.quantity) AS totalQuantity,
               SUM(s.quantity * mp.menuPrice) AS totalAmount,
               TO_CHAR(s.saleTime, 'YYYY-MM-DD') AS saleTime
          FROM sales s
         JOIN store st ON s.storeId = st.id
         JOIN menuprice mp ON s.menuId = mp.menuId
         WHERE (:storeId IS NULL OR s.storeId = :storeId)
           AND (:startDate IS NULL OR s.saleTime >= :startDate)
           AND (:endDate IS NULL OR s.saleTime <= :endDate)
         GROUP BY s.storeId, st.name, s.menuId, s.menuName, TO_CHAR(s.saleTime, 'YYYY-MM-DD')
         ORDER BY saleTime
        """, nativeQuery = true)
    List<SalesSummaryDto> findSalesSummaryByDate(
            @Param("storeId") Integer storeId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );


    // 2. 매장+메뉴별 합계(날짜 상관없이)
    @Query(value = """
    SELECT s.storeId AS storeId,
           st.name AS storeName,
           s.menuId AS menuId,
           s.menuName AS menuName,
           SUM(s.quantity) AS totalQuantity,
           SUM(s.quantity * mp.menuPrice) AS totalAmount
    FROM sales s
    JOIN store st ON s.storeId = st.id
    JOIN menuprice mp ON s.menuId = mp.menuId
    WHERE (:storeId IS NULL OR s.storeId = :storeId)
      AND (:startDate IS NULL OR s.saleTime >= :startDate)
      AND (:endDate IS NULL OR s.saleTime <= :endDate)
    GROUP BY s.storeId, st.name, s.menuId, s.menuName
    ORDER BY s.storeId, s.menuId
""", nativeQuery = true)
    List<SalesSummaryDto> findSalesSummaryTotal(
            @Param("storeId") Integer storeId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );


    @Query(value = """
SELECT s.storeId AS storeId,
       st.name AS storeName,
       s.menuId AS menuId,
       s.menuName AS menuName,
       CAST(SUM(s.quantity) AS DECIMAL(10,2)) AS totalQuantity,
       CAST(SUM(s.quantity * mp.menuPrice) AS DECIMAL(10,2)) AS totalAmount,
       TO_CHAR(s.saleTime, 'YYYY-MM-DD') AS saleTime
FROM SALES s
JOIN STORE st ON s.storeId = st.id
JOIN MENUPRICE mp ON s.menuId = mp.menuId
WHERE s.storeId = :storeId
  AND s.saleTime BETWEEN :startDate AND :endDate
GROUP BY s.storeId, st.name, s.menuId, s.menuName, TO_CHAR(s.saleTime, 'YYYY-MM-DD')
ORDER BY saleTime
""", nativeQuery = true)
    List<SalesSummaryDto> findSummaryByStoreAndDate(
            @Param("storeId") Integer storeId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("""
    SELECT COALESCE(SUM(s.quantity), 0)
    FROM Sales s
    WHERE s.storeId = :storeId
      AND s.menuId = :menuId
      AND s.saleTime BETWEEN :startDate AND :endDate
""")
    int sumQuantityByStoreIdAndMenuIdAndPeriod(
            @Param("storeId") Integer storeId,
            @Param("menuId") String menuId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );

    @Query(value = """
    SELECT s.storeId AS storeId,
           st.name AS storeName,
           s.menuId AS menuId,
           s.menuName AS menuName,
           SUM(s.quantity) AS totalQuantity,
           SUM(s.quantity * mp.menuPrice) AS totalAmount
      FROM sales s
     JOIN store st ON s.storeId = st.id
     JOIN menuprice mp ON s.menuId = mp.menuId
     WHERE s.storeId = :storeId
       AND s.saleTime BETWEEN :startDate AND :endDate
       AND (:menuName IS NULL OR s.menuName LIKE :menuName)
     GROUP BY s.storeId, st.name, s.menuId, s.menuName
     ORDER BY s.menuId
     """, nativeQuery = true)
    List<SalesSummaryDto> findMenuSummary(
            @Param("storeId") Integer storeId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("menuName") String menuName
    );
}