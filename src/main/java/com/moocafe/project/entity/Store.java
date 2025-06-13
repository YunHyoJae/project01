package com.moocafe.project.entity;

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
public class Store extends BaseEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Integer id;

    @OneToMany(mappedBy = "store",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberStore> members = new ArrayList<>();
    @Column(nullable=false, columnDefinition = "VARCHAR(30)")
    private String name;
    @Column(unique=true, nullable=false, columnDefinition = "VARCHAR(12)")
    private String storeNumber;
    private String space;
    private String tel;
    private String zipcode;
    private String address01;
    private String address02;
    private String state ="오픈예정";
    public List<MemberStore> getMembers() {
        if (members == null){
            members = new ArrayList<>();
        }
        return members;
    }
    public void updateStore(MemberStoreDto msd){
        this.name=msd.getName();
        this.storeNumber=msd.getStoreNumber();
        this.space=msd.getSpace();
        this.tel=msd.getTel();
        this.zipcode=msd.getZipcode();
        this.address01=msd.getAddress01();
        this.address02=msd.getAddress02();
        this.state=msd.getState()==null?this.state:msd.getState();
    }
}
