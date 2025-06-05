package com.moocafe.project.dto;

import com.moocafe.project.entity.FranchiseBoard;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class FranchiseBoardSaveDto {
    @NotBlank(message = "이름은 필수입력사항입니다.")
    private String name;
    @NotBlank(message = "연락처는 필수 입력사항입니다.")
    private String tel;
    @Email(message = "이메일형식에 맞게 입력해주세요.")
    private String email;
    private String space;
    private int store;
    private String time;
    private String course;
    @NotBlank(message = "상담내용을 입력해주세요.")
    private String content;

    public static FranchiseBoard toEntity(FranchiseBoardSaveDto dto) {
        return FranchiseBoard.builder()
                .name(dto.getName())
                .tel(dto.getTel())
                .email(dto.getEmail())
                .space(dto.getSpace())
                .store(dto.getStore())
                .time(dto.getTime())
                .course(dto.getCourse())
                .content(dto.getContent())
                .state("상담신청")
                .build();
    }
}
