package com.moocafe.project.dto;

import com.moocafe.project.entity.FranchiseReply;
import lombok.*;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class FranchiseReplyDto {
    private Integer id;
    private int boardId;
    private String memberId;
    private String memberName;
    private String content;
    private String regDate;
    private String state;

    public static FranchiseReplyDto toDto(FranchiseReply entity) {
        return FranchiseReplyDto.builder()
                .id(entity.getId())
                .boardId(entity.getBoard().getId())
                .memberId(entity.getMember().getUserId())
                .memberName(entity.getMember().getUserName())
                .content(entity.getContent())
                .regDate(entity.getRegDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .state(entity.getState())
                .build();
    }
}
