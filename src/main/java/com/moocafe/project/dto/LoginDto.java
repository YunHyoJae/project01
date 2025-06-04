package com.moocafe.project.dto;

import com.moocafe.project.entity.Member;
import com.moocafe.project.entity.MemberStore;
import com.moocafe.project.entity.Store;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class LoginDto {
    private String userId;
    private String userName;
    private int storeId;
    private String storeName;

    public static LoginDto toDto(MemberStore memberStore) {
        return LoginDto.builder()
                .userId(memberStore.getMember().getUserId())
                .userName(memberStore.getMember().getUserName())
                .storeId(memberStore.getStore().getId())
                .storeName(memberStore.getStore().getName())
                .build();
    };
    public static LoginDto toDto(Member member, Store store) {
        return LoginDto.builder()
                .userId(member.getUserId())
                .userName(member.getUserName())
                .storeId(store.getId())
                .storeName(store.getName())
                .build();
    }
}
