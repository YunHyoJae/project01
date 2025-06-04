package com.moocafe.project.dto;

import com.moocafe.project.constent.Role;
import com.moocafe.project.entity.Member;
import com.moocafe.project.entity.MemberStore;
import com.moocafe.project.entity.Store;
import jakarta.persistence.Column;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MemberStoreDto {
    private String userId;
    private String userPw;
    private String userName;
    private String userTel;
    private String userEmail;
    private String userZipcode;
    private String userAddress01;
    private String userAddress02;
    private Role role;

    private String name;
    private String storeNumber;
    private String space;
    private String tel;
    private String zipcode;
    private String address01;
    private String address02;
    private String state;

    public static Member toMemberEntity(MemberStoreDto dto) {
        return Member.builder()
                .userId(dto.getUserId())
                .userPw(dto.getUserPw())
                .userName(dto.getUserName())
                .tel(dto.getUserTel())
                .email(dto.getUserEmail())
                .zipcode(dto.getUserZipcode())
                .address01(dto.getUserAddress01())
                .address02(dto.getUserAddress02())
                .role(dto.getRole())
                .build();
    }
    public static Store toStoreEntity(MemberStoreDto dto) {
        return Store.builder()
                .name(dto.getName())
                .storeNumber(dto.getStoreNumber())
                .space(dto.getSpace())
                .tel(dto.getTel())
                .zipcode(dto.getZipcode())
                .address01(dto.getAddress01())
                .address02(dto.getAddress02())
                .state(dto.getState())
                .build();
    }
}
