package com.moocafe.project.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.moocafe.project.entity.Faq;
import com.moocafe.project.entity.FaqReply;
import com.moocafe.project.entity.Member;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class FaqReplySaveDto {
    private Integer id;
    @JsonIgnore
    private Faq board;
    private Member member;
    private String content;
    private LocalDateTime regDate;
    private String state;

    public static FaqReply toEntity(FaqReplySaveDto dto) {
        return FaqReply.builder()
                .board(dto.getBoard())
                .member(dto.getMember())
                .content(dto.getContent())
                .state("답변완료")
                .build();
    }
}
