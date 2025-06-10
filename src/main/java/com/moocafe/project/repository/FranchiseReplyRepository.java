package com.moocafe.project.repository;

import com.moocafe.project.entity.FranchiseBoard;
import com.moocafe.project.entity.FranchiseReply;
import com.moocafe.project.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FranchiseReplyRepository extends JpaRepository<FranchiseReply, Integer> {
    boolean existsByBoardAndMember(FranchiseBoard board, Member member);
}
