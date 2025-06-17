package com.moocafe.project.repository;

import com.moocafe.project.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Integer> {
    List<Store> findByNameContaining(String name);
    @Query("SELECT s FROM Store s WHERE s.id <> :excludedId")
    Page<Store> findAllExcept(@Param("excludedId") Integer excludedId, Pageable pageable);
    @Query("SELECT s FROM Store s WHERE (:keyword IS NULL OR s.name LIKE %:keyword%) AND s.id <> :id")
    Page<Store> findByNameExcept(int id, @Param("keyword") String keyword, Pageable pageable);
    @Query("SELECT s FROM Store s WHERE (:keyword IS NULL OR s.space LIKE %:keyword%) AND s.id <> :id")
    Page<Store> findBySpaceExcept(int id, @Param("keyword") String keyword, Pageable pageable);
    @Query("SELECT s FROM Store s WHERE (:keyword IS NULL OR s.state = :keyword) AND s.id <> :id")
    Page<Store> findByStateExcept(int id, @Param("keyword") String keyword, Pageable pageable);
    @Query("""
       SELECT s FROM Store s
       LEFT JOIN FETCH s.members ms
       LEFT JOIN FETCH ms.member m
       WHERE (:keyword IS NULL OR m.userName LIKE %:keyword%) AND s.id <> :id
       """)
    Page<Store> findByUserNameExcept(int id, @Param("keyword") String keyword, Pageable pageable);
    @Query("""
       SELECT s FROM Store s
       LEFT JOIN FETCH s.members ms
       LEFT JOIN FETCH ms.member m
       WHERE (:keyword IS NULL OR m.tel LIKE %:keyword%) AND s.id <> :id
       """)
    Page<Store> findByUserTelExcept(int id, @Param("keyword") String keyword, Pageable pageable);
    Optional<Store> findByStoreNumber(String storeNumber);

    @Query("SELECT s.id FROM Store s JOIN s.members ms WHERE ms.member.id = :memberId")
    Integer findStoreIdByMemberId(@Param("memberId") Integer memberId);
}
