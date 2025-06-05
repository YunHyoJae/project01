package com.moocafe.project.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name="faqBoard")
@EntityListeners(AuditingEntityListener.class)
public class Faq {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Integer id;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="userId", referencedColumnName = "userId")
    private Member member;
    private String title;
    private String category01;
    private String category02;
    private String content;
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime regDate;
    private String state;
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<FaqReply> faqReplyList;
}
