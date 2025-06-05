package com.moocafe.project.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.moocafe.project.entity.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class FranchiseReplySaveDto {
    private Integer id;
    @JsonIgnore
    private FranchiseBoard board;
    private Member member;
    private String content;
    private LocalDateTime regDate;
    private String state;

    public static FranchiseReply toEntity(FranchiseReplySaveDto dto) {
        return FranchiseReply.builder()
                .board(dto.getBoard())
                .member(dto.getMember())
                .content(dto.getContent())
                .state("답변완료")
                .build();
    }
}
