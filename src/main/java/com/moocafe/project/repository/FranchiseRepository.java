package com.moocafe.project.repository;

import com.moocafe.project.entity.FranchiseBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FranchiseRepository extends JpaRepository<FranchiseBoard, Integer> {
}
