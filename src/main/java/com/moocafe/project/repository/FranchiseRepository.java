package com.moocafe.project.repository;

import com.moocafe.project.entity.FranchiseBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FranchiseRepository extends JpaRepository<FranchiseBoard, Integer> {
    Page<FranchiseBoard> findByName(String name, Pageable pageable);
    Page<FranchiseBoard> findByTel(String tel, Pageable pageable);
    Page<FranchiseBoard> findByState(String state, Pageable pageable);

    Page<FranchiseBoard> findByNameContaining(String name, Pageable pageable);

    Page<FranchiseBoard> findByTelContaining(String tel, Pageable pageable);
}
