package com.moocafe.project.repository;

import com.moocafe.project.entity.FranchiseReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FranchiseReplyRepository extends JpaRepository<FranchiseReply, Integer> {
}
