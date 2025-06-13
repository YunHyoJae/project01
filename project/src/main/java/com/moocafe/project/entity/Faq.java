package com.moocafe.project.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name="faqBoard")
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
    private String regDate;
    private String state;
}
