package com.moocafe.project.repository;

import com.moocafe.project.entity.FaqReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FaqReplyRepository extends JpaRepository<FaqReply, Integer> {
}
