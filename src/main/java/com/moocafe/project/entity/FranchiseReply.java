package com.moocafe.project.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class FranchiseReply {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Integer id;
    @OneToOne(fetch=FetchType.LAZY)
    private FranchiseBoard board;
    @ManyToOne(fetch=FetchType.LAZY)
    private Member member;
    private String content;
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime regDate = LocalDateTime.now();
    private String state;
}
