package com.moocafe.project.service;

import com.moocafe.project.constent.Role;
import com.moocafe.project.dao.MemberDao;
import com.moocafe.project.dao.MemberStoreDao;
import com.moocafe.project.dao.StoreDao;
import com.moocafe.project.dto.MemberStoreDto;
import com.moocafe.project.entity.Member;
import com.moocafe.project.entity.MemberStore;
import com.moocafe.project.entity.Store;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberStoreService {
    private final MemberDao memberDao;
    private final StoreDao storeDao;
    private final MemberStoreDao memberStoreDao;
    @Transactional
    public void save(MemberStoreDto ms) {
        ms.setRole(Role.ROLE_USER);
        Member member=MemberStoreDto.toMemberEntity(ms);
        Store store=MemberStoreDto.toStoreEntity(ms);
        MemberStore memberStore = MemberStore.builder()
                .member(member)
                .store(store)
                .build();
        member.getStores().add(memberStore);
        store.getMembers().add(memberStore);
        memberDao.save(member);
        storeDao.save(store);
        memberStoreDao.save(memberStore);
    }
    @Transactional
    public void save(Member member,Store store) {
        MemberStore memberStore = MemberStore.builder()
                .member(member)
                .store(store)
                .build();
        member.getStores().add(memberStore);
        store.getMembers().add(memberStore);
        memberDao.save(member);
        storeDao.save(store);
        memberStoreDao.save(memberStore);
    }
}
