package com.moocafe.project.repository;

import com.moocafe.project.dto.StoreOrderListResponseDto;
import com.moocafe.project.entity.StoreOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreOrderRepository extends JpaRepository<StoreOrder, Integer> {

    @Query(
            value = "SELECT COUNT(*) FROM StoreOrder " +
                    "WHERE storeId = :storeId AND TO_CHAR(orderDate, 'YYYYMMDD') = :today",
            nativeQuery = true
    )
    int countByStoreIdAndDate(@Param("storeId") Integer storeId, @Param("today") String today);
    //Optional<StoreOrder> findById(Integer id);


    /**
     * 매장별 최근 4건의 주문 내역을 가져오는 쿼리 (오라클 기준)
     */
    @Query(
            value =
                    "SELECT o.ORDERNUMBER, " +
                            "       TO_CHAR(o.ORDERDATE, 'YYYY-MM-DD') AS ORDERDATE, " +
                            "       d.ITEMCODE, " +
                            "       i.ITEMNAME, " +
                            "       d.ORDEREDQUANTITY, " +
                            "       d.STATUS " +
                            "FROM STOREORDER o " +
                            "JOIN STOREORDERDETAIL d ON o.ID = d.ORDERID " +
                            "JOIN INVENTORYREGISTRATION i ON d.ITEMCODE = i.ITEMCODE " +
                            "WHERE o.STOREID = :storeId " +
                            "ORDER BY o.ORDERDATE DESC " +
                            "FETCH FIRST 4 ROWS ONLY",
            nativeQuery = true
    )
    List<Object[]> findRecentOrderListByStoreId(@Param("storeId") Integer storeId);
}