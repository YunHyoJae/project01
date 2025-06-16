package com.moocafe.project.repository;

import com.moocafe.project.entity.ReturnItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface ReturnItemRepository extends JpaRepository<ReturnItem, Integer> {

    @Query("SELECT ri FROM ReturnItem ri " +
            "JOIN ri.returnEntity r " +
            "JOIN r.orderNumber so " +
            "WHERE so.storeId = :storeId")
    List<ReturnItem> findByStoreId(@Param("storeId") int storeId);

}
