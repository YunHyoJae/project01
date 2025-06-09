package com.moocafe.project.dto;

import com.moocafe.project.entity.FaqReply;
import lombok.*;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class FaqReplyDto {
    private Integer id;
    private int boardId;
    private String memberId;
    private String memberName;
    private String content;
    private String regDate;
    private String state;

    public static FaqReplyDto toDto(FaqReply faqReply) {
        return FaqReplyDto.builder()
                .id(faqReply.getId())
                .boardId(faqReply.getBoard().getId())
                .memberId(faqReply.getMember().getUserId())
                .memberName(faqReply.getMember().getUserName())
                .content(faqReply.getContent())
                .regDate(faqReply.getRegDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .state(faqReply.getState())
                .build();
    }
}
