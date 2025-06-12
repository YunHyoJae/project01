package com.moocafe.project.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class MemberStore {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Integer id;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Member member;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Store store;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime regDate = LocalDateTime.now();

    public static MemberStore toEntity(Member member, Store store) {
        return MemberStore.builder().member(member).store(store).build();
    }
}
