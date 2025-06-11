package com.moocafe.project.dao;

import com.moocafe.project.entity.Member;
import com.moocafe.project.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberDao {
    private final MemberRepository memberRepository;
    public Member save(Member member) {
        return memberRepository.save(member);
    }
    public Optional<Member> findByUserId(String userID) {
        return memberRepository.findByUserId(userID);
    }
    public List<Member> findAll() {
        return memberRepository.findAll();
    }
    public int deleteByUserID(String userID) {
        return memberRepository.deleteByUserId(userID);
    }
}
