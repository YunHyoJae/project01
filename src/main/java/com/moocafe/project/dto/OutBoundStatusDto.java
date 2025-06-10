package com.moocafe.project.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OutBoundStatusDto {
    private Integer outBoundId;
    private String status; //상태
}
