package com.moocafe.project.entity;

import com.moocafe.project.constent.Role;
import com.moocafe.project.dto.MemberStoreDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
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
    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberStore> stores = new ArrayList<>();
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "member",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Faq> faqList;
    @OneToMany(mappedBy = "member",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<FranchiseReply> franchiseReplyList;

    public List<MemberStore> getStores() {
        if (stores == null){
            stores = new ArrayList<>();
        }
        return stores;
    }
    public void updateMember(MemberStoreDto msd){
        this.userName=msd.getUserName();
        this.tel=msd.getUserTel();
        this.email=msd.getUserEmail();
        this.zipcode=msd.getUserZipcode();
        this.address01=msd.getUserAddress01();
        this.address02=msd.getUserAddress02();
    }

}
