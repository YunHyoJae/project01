package com.moocafe.project.repository;

import com.moocafe.project.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
    Optional<Member> findByUserId(String userId);

    @Modifying
    @Transactional
        //@Query("DELETE FROM Member m where m.userId = :userId")
    int deleteByUserId(String userId);
}
