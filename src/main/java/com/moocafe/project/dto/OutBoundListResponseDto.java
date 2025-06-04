package com.moocafe.project.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OutBoundListResponseDto {
    private Long OutBoundId;
    private String StoreName;
    private String ItemCode;
    private String ItemName;
    private int ReceivedQuantity;
    private String RequiredDate;
    private String DueDate;
    private String status;
}
