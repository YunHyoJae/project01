package com.moocafe.project.dao;

import com.moocafe.project.entity.*;
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
    public int save(FaqReply fr) {
        FaqReply savedEntity = frr.save(fr);
        return (savedEntity.getId() != null) ? 1 : 0;
    }
}
