package com.moocafe.project.entity;

import com.moocafe.project.constent.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Integer id;

    @Column(unique=true, nullable=false, columnDefinition = "VARCHAR(100)")
    private String userId;
    @Column(nullable=false)
    private String userPw;
    private String userName;
    private String tel;
    @Column(unique = true, nullable = false)
    private String email;
    private String zipcode;
    private String address01;
    private String address02;
    private int storeId;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "member",fetch = FetchType.LAZY,cascade = CascadeType.ALL) //mappedBy는 테이블에 컬럼을 만들지 않겠다.
    private List<Faq> faqList;

}
