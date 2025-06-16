package com.moocafe.project.dto;

import com.moocafe.project.entity.Faq;
import com.moocafe.project.entity.Member;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class FaqBoardSaveDto {
    private Integer id;
    private Member member;
    @NotBlank(message = "제목을 입력해주세요.")
    private String title;
    private String category01;
    private String category02;
    @NotBlank(message = "상담내용을 입력해주세요.")
    private String content;
    private Boolean state;

    public static Faq toEntity(FaqBoardSaveDto dto) {
        return Faq.builder()
                .member(dto.getMember())
                .title(dto.getTitle())
                .category01(dto.getCategory01())
                .category02(dto.getCategory02())
                .content(dto.getContent())
                .state(Boolean.TRUE.equals(dto.getState()) ? "비공개" : "공개")
                .build();
    }
}
