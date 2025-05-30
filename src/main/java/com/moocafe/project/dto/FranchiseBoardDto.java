package com.moocafe.project.dto;

import lombok.*;

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
    private int store;
    private String time;
    private String course;
    private String content;
    private String regDate;
    private String state;
    private FaqReplyDto faqReply;
}
