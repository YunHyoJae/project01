package com.moocafe.project.dao;

import com.moocafe.project.entity.MemberStore;
import com.moocafe.project.repository.MemberStoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberStoreDao {
    private final MemberStoreRepository memberStoreRepository;
    public void save(MemberStore memberStore) {
        memberStoreRepository.save(memberStore);
    }
    public Optional<MemberStore> findById(int id) {return memberStoreRepository.findById(id);}
    public List<MemberStore> findByMemberId(int id) {return memberStoreRepository.findByMemberId(id);}
    public List<MemberStore> findByStoreId(int id){return memberStoreRepository.findByStoreId(id);}
}
