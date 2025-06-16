package com.moocafe.project.service;

import com.moocafe.project.constent.Role;
import com.moocafe.project.dao.MemberDao;
import com.moocafe.project.dao.MemberStoreDao;
import com.moocafe.project.dao.StoreDao;
import com.moocafe.project.dto.FranchiseBoardDto;
import com.moocafe.project.dto.MemberStoreDto;
import com.moocafe.project.entity.FranchiseBoard;
import com.moocafe.project.entity.Member;
import com.moocafe.project.entity.MemberStore;
import com.moocafe.project.entity.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberStoreService {
    private final MemberDao memberDao;
    private final StoreDao storeDao;
    private final MemberStoreDao memberStoreDao;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    public List<MemberStoreDto> getAll() {
        List<Store> stores = storeDao.findAll();
        List<MemberStoreDto> result = new ArrayList<>();
        for (Store store : stores) {
            if(store.getId()!=1) {
                List<MemberStore> memberStore = memberStoreDao.findByStoreId(store.getId());
                result.add(MemberStoreDto.toDto(memberStore.getFirst()));
            }
        };
        return result;
    }
    public Page<MemberStoreDto> getAllPage(String type, String keyword, Pageable pageable) {
        Page<Store> stores;
        switch (type) {
            case "userName" -> { stores = storeDao.findByUserNameExcept(1, keyword ,pageable); }
            case "userTel" -> { stores = storeDao.findByUserTelExcept(1, keyword, pageable); }
            case "name" -> { stores = storeDao.findByNameExcept(1, keyword, pageable); }
            case "space" -> { stores = storeDao.findBySpaceExcept(1, keyword, pageable); }
            case "state" -> { stores = storeDao.findByStateExcept(1, keyword, pageable); }
            default -> { stores = storeDao.findAllExcept(1, pageable); }
        };
        List<MemberStoreDto> result = new ArrayList<>();
        for (Store store : stores) {
            List<MemberStore> memberStore = memberStoreDao.findByStoreId(store.getId());
            result.add(MemberStoreDto.toDto(memberStore.getFirst()));
        };
        return new PageImpl<>(result, pageable, stores.getTotalElements());
    }

    public MemberStoreDto getMemberStore(Member user) {
        List<MemberStore> memberStore = memberStoreDao.findByMemberId(user.getId());
        return MemberStoreDto.toDto(memberStore.getFirst());
    }
    public MemberStoreDto getStoreMember(int storeId) {
        List<MemberStore> memberStore = memberStoreDao.findByStoreId(storeId);
        return MemberStoreDto.toDto(memberStore.getFirst());
    }
    @Transactional
    public void save(MemberStoreDto ms) {
        ms.setRole(Role.ROLE_USER);
        String pw=ms.getUserPw();
        ms.setUserPw(bCryptPasswordEncoder.encode(pw));
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
    @Transactional
    public int update(MemberStoreDto ms) {
        Optional<MemberStore> memberStoreOpt = memberStoreDao.findByMemberIdAndStoreId(ms.getMemberId(), ms.getStoreId());
        memberStoreOpt.ifPresent(memberStore -> {
            memberStore.getMember().updateMember(ms);
            memberStore.getStore().updateStore(ms);
            memberDao.save(memberStore.getMember());  // 부모 저장
            storeDao.save(memberStore.getStore());    // 부모 저장
            memberStoreDao.save(memberStore);         // 자식 저장
        });
        return memberStoreOpt.isPresent() ? 1 : 0;
    }
    public String findUserId(String userId){
        Optional<Member> member=memberDao.findByUserId(userId);
        return member.map(Member::getUserId).orElse(null);
    }
    public String findByStoreNumber(String sn){
        Optional<Store> store=storeDao.findByStoreNumber(sn);
        return store.map(Store::getStoreNumber).orElse(null);
    }
}
