package com.moocafe.project.service;

import com.moocafe.project.constent.Role;
import com.moocafe.project.dao.MemberDao;
import com.moocafe.project.dao.MemberStoreDao;
import com.moocafe.project.dao.StoreDao;
import com.moocafe.project.dto.MemberStoreDto;
import com.moocafe.project.entity.Member;
import com.moocafe.project.entity.MemberStore;
import com.moocafe.project.entity.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberStoreService {
    private final MemberDao memberDao;
    private final StoreDao storeDao;
    private final MemberStoreDao memberStoreDao;
    @Transactional(readOnly = true)
    public List<MemberStoreDto> getAll() {
//        List<Store> stores = storeDao.findAll();
//        stores.stream().forEach(store -> {
//            if(store){}
//        })
//        List<Member> members = memberDao.findAll();
//        List<MemberStore> memberStore = memberStoreDao.findByMemberId(user.getId());
//        return MemberStoreDto.toDto(memberStore.getFirst());
        return null;
    }
    @Transactional(readOnly = true)
    public MemberStoreDto getMemberStore(Member user) {
        List<MemberStore> memberStore = memberStoreDao.findByMemberId(user.getId());
        return MemberStoreDto.toDto(memberStore.getFirst());
    }
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
//        memberDao.save(member);
//        storeDao.save(store);
        memberStoreDao.save(memberStore);
    }
    public void save(Member member,Store store) {
        MemberStore memberStore = MemberStore.builder()
                .member(member)
                .store(store)
                .build();
        member.getStores().add(memberStore);
        store.getMembers().add(memberStore);
//        memberDao.save(member);
//        storeDao.save(store);
        memberStoreDao.save(memberStore);
    }
    public int update(MemberStoreDto ms) {
        Optional<MemberStore> memberStoreOpt = memberStoreDao.findByMemberIdAndStoreId(ms.getMemberId(), ms.getStoreId());
        if (memberStoreOpt.isPresent()) {
            MemberStore memberStore = memberStoreOpt.get();
            memberStore.getMember().updateMember(ms);
            memberStore.getStore().updateStore(ms);
            memberStoreDao.save(memberStore); // 기존 객체 수정 후 저장
            return 1;
        }
        return 0;
    }
}
