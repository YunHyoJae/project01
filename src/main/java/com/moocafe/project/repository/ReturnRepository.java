package com.moocafe.project.repository;

import com.moocafe.project.entity.Return;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReturnRepository extends JpaRepository<Return, Integer> {
    // ReturnRepository.java
//    @Query("SELECT r FROM Return r WHERE r.orderNumber.storeId = :storeId")
//    List<Return> findByStoreId(@Param("storeId") Integer storeId);

    @Query("SELECT r FROM Return r JOIN FETCH r.items WHERE r.orderNumber.storeId = :storeId")
    List<Return> findByStoreIdWithItems(@Param("storeId") Integer storeId);

}
