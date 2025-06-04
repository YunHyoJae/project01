package com.moocafe.project.repository;

import com.moocafe.project.entity.MemberStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MemberStoreRepository extends JpaRepository<MemberStore, Integer> {
}
