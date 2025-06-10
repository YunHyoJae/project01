package com.moocafe.project.dao;

import com.moocafe.project.entity.FaqReply;
import com.moocafe.project.entity.FranchiseBoard;
import com.moocafe.project.entity.FranchiseReply;
import com.moocafe.project.entity.Member;
import com.moocafe.project.repository.FaqReplyRepository;
import com.moocafe.project.repository.FranchiseReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FaqReplyDao {
    private final FaqReplyRepository frr;
    public void save(FaqReply fr) {
        frr.save(fr);
    }
}
