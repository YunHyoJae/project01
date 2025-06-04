package com.moocafe.project.repository;

import com.moocafe.project.entity.OutBoundItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutBoundItemRepository extends JpaRepository<OutBoundItem, Long> {
}
