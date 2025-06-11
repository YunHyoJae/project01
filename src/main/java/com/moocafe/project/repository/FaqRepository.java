package com.moocafe.project.repository;

import com.moocafe.project.entity.Faq;
import com.moocafe.project.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FaqRepository extends JpaRepository<Faq, Integer> {

    Page<Faq> findByTitleContaining(String title, Pageable pageable);

    Page<Faq> findByMember(Member member, Pageable pageable);

    Page<Faq> findByCategory01(String category01, Pageable pageable);

    Page<Faq> findByMember_userNameContaining(String memberUserName, Pageable pageable);

    Page<Faq> findByState(String state, Pageable pageable);

    Page<Faq> findByCategory01ContainingAndMember(String category01, Member member, Pageable pageable);

    Page<Faq> findByTitleContainingAndMember(String title, Member member, Pageable pageable);
}
