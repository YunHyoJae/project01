package com.moocafe.project.dto;

import com.moocafe.project.constent.Role;
import com.moocafe.project.entity.Member;
import com.moocafe.project.entity.MemberStore;
import com.moocafe.project.entity.Store;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MemberStoreDto {
    private int memberId;
    @NotBlank(message = "아이디를 입력해주세요.")
    private String userId;
    @NotBlank(message = "패스워드를 입력해주세요.")
    private String userPw;
    @NotBlank(message = "고객명을 입력해주세요.")
    private String userName;
    @NotBlank(message = "고객 전화번호를 입력해주세요.")
    private String userTel;
    private String userEmail;
    private String userZipcode;
    private String userAddress01;
    private String userAddress02;
    private String regDate;
    private Role role;

    private int storeId;
    @NotBlank(message = "상점 이름을 입력해주세요.")
    private String name;
    private String storeNumber;
    @NotBlank(message = "점포 위치는 필수 값 입니다.")
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
    public static MemberStoreDto toDto(MemberStore entity) {
        return MemberStoreDto.builder()
                .memberId(entity.getMember().getId())
                .userId(entity.getMember().getUserId())
                .userPw(entity.getMember().getUserPw())
                .userName(entity.getMember().getUserName())
                .userTel(entity.getMember().getTel())
                .userEmail(entity.getMember().getEmail())
                .userZipcode(entity.getMember().getZipcode())
                .userAddress01(entity.getMember().getAddress01())
                .userAddress02(entity.getMember().getAddress02())
                .role(entity.getMember().getRole())
                .name(entity.getStore().getName())
                .storeId(entity.getStore().getId())
                .storeNumber(entity.getStore().getStoreNumber())
                .space(entity.getStore().getSpace())
                .tel(entity.getStore().getTel())
                .zipcode(entity.getStore().getZipcode())
                .address01(entity.getStore().getAddress01())
                .address02(entity.getStore().getAddress02())
                .state(entity.getStore().getState())
                .regDate(entity.getRegDate()!=null?entity.getRegDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")):null)
                .build();
    }
}
