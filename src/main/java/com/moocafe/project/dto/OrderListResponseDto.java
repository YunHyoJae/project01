package com.moocafe.project.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderListResponseDto {
    private String OrderNumber;
    private String OrderDate;
    private String ItemCode;
    private String ItemName;
    private int OrderedQuantity;
    private String status;
}
