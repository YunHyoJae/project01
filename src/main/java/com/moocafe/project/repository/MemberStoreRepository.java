package com.moocafe.project.repository;

import com.moocafe.project.entity.MemberStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface MemberStoreRepository extends JpaRepository<MemberStore, Integer> {
    List<MemberStore> findByMemberId(int memberId);
    List<MemberStore> findByStoreId(int storeId);
    Optional<MemberStore> findByMemberIdAndStoreId(Integer memberId, Integer storeId);
}
