package com.moocafe.project.repository;

import com.moocafe.project.entity.MemberStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MemberStoreRepository extends JpaRepository<MemberStore, Integer> {
    public List<MemberStore> findByMemberId(int memberId);
    public List<MemberStore> findByStoreId(int storeId);
}
