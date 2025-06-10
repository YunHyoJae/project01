package com.moocafe.project.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OutBoundStatusDto {
    private List<Integer> outBoundIds;
    private String status; //상태
}
