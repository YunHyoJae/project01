package com.moocafe.project.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OutBoundSearchDto {
    private String startDate;
    private String endDate;
    private String name; //store name
}
