package com.moocafe.project.dao;

import com.moocafe.project.entity.FranchiseBoard;
import com.moocafe.project.entity.FranchiseReply;
import com.moocafe.project.entity.Member;
import com.moocafe.project.repository.FranchiseReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FranchiseReplyDao {
    private final FranchiseReplyRepository frr;
    public void save(FranchiseReply fr) {
        frr.save(fr);
    }
    public Optional<FranchiseReply> findById(Integer id) {return frr.findById(id);}
    public boolean existsByBoardAndMember(FranchiseBoard board, Member member) {return frr.existsByBoardAndMember(board, member);}
}
