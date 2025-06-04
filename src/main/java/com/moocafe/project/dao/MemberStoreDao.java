package com.moocafe.project.dao;

import com.moocafe.project.entity.MemberStore;
import com.moocafe.project.repository.MemberStoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberStoreDao {
    private final MemberStoreRepository memberStoreRepository;
    public void save(MemberStore memberStore) {
        memberStoreRepository.save(memberStore);
    }
}
