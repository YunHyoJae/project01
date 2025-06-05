package com.moocafe.project.repository;

import com.moocafe.project.entity.OutBoundItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutBoundItemRepository extends JpaRepository<OutBoundItem, Integer> {
    @Query(value = "SELECT * FROM OutBoundItem WHERE OutBoundId = :outBoundId", nativeQuery = true)
    List<OutBoundItem> findByOutBoundId(@Param("outBoundId") Long outBoundId);
}
