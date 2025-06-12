package com.moocafe.project.dto;

import com.moocafe.project.entity.FranchiseBoard;
import lombok.*;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class FranchiseBoardDto {
    private int id;
    private String name;
    private String tel;
    private String email;
    private String space;
    private String store;
    private String time;
    private String course;
    private String content;
    private String regDate;
    private String state;
    private FranchiseReplyDto reply;

    public static FranchiseBoardDto toDto(FranchiseBoard entity) {
        return FranchiseBoardDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .tel(entity.getTel())
                .email(entity.getEmail())
                .space(entity.getSpace())
                .store(entity.getStore())
                .time(entity.getTime())
                .course(entity.getCourse())
                .content(entity.getContent())
                .regDate(entity.getRegDate()!=null?entity.getRegDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")):null)
                .state(entity.getState())
                .reply(entity.getReply()!=null?FranchiseReplyDto.toDto(entity.getReply()):null)
                .build();
    }
}
