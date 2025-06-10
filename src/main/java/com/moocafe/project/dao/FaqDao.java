package com.moocafe.project.dao;

import com.moocafe.project.entity.Faq;
import com.moocafe.project.entity.FranchiseBoard;
import com.moocafe.project.entity.Member;
import com.moocafe.project.repository.FaqRepository;
import com.moocafe.project.repository.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FaqDao {
    private final FaqRepository fr;
    public int save(Faq entity) {
        Faq savedEntity = fr.save(entity);
        return (savedEntity.getId() != null) ? 1 : 0;
    }
    public List<Faq> list() {
        return fr.findAll();
    }
    public Optional<Faq> findById(int id) {
        return fr.findById(id);
    }
    public Page<Faq> findAll(Pageable pageable) {return fr.findAll(pageable);}
    public Page<Faq> findByTitle(String keyword, Pageable pageable) {return fr.findByTitleContaining(keyword, pageable);}
    public Page<Faq> findByCategory(String keyword, Pageable pageable) {return fr.findByCategory01(keyword, pageable);}
    public Page<Faq> findByState(String keyword, Pageable pageable) {return fr.findByState(keyword, pageable);}
    public Page<Faq> findByMember_NameContaining(String keyword, Pageable pageable) {
        return fr.findByMember_userNameContaining(keyword, pageable);
    }
    public Page<Faq> findByMember(Member member, Pageable pageable) {return fr.findByMemberContaining(member, pageable);}
    public Page<Faq> findByTitleAndMember(String keyword, Member member, Pageable pageable) {return fr.findByTitleContainingAndMember(keyword, member,pageable);}
    public Page<Faq> findByCategoryAndMember(String keyword, Member member, Pageable pageable) {return fr.findByCategory01ContainingAndMember(keyword, member,pageable);}
}
