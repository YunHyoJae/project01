package com.moocafe.project.repository;

import com.moocafe.project.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store, Integer> {
    List<Store> findByNameContaining(String name);
}
