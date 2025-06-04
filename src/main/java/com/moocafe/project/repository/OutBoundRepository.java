package com.moocafe.project.repository;

import com.moocafe.project.entity.OutBound;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutBoundRepository extends JpaRepository<OutBound, Long> {
}
