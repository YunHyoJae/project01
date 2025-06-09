package com.moocafe.project.dto;

import com.moocafe.project.entity.Faq;
import lombok.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class FaqBoardDto {
    private Integer id;
    private String memberId;
    private String memberName;
    private String title;
    private String category01;
    private String category02;
    private String content;
    private String regDate;
    private String state;
    private List<FaqReplyDto> faqReplyList;

    public static FaqBoardDto toDto(Faq entity) {
        return FaqBoardDto.builder()
                .id(entity.getId())
                .memberId(entity.getMember().getUserId())
                .memberName(entity.getMember().getUserName())
                .title(entity.getTitle())
                .category01(entity.getCategory01())
                .category02(entity.getCategory02())
                .content(entity.getContent())
                .regDate(entity.getRegDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .state(entity.getState())
                .faqReplyList(entity.getFaqReplyList().stream().map(FaqReplyDto::toDto).toList())
                .build();
    }
}
