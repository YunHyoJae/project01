package com.moocafe.project.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutBoundListResponseDto {
    private Integer outBoundId;
    private String storeName;
    private String itemCode;
    private String itemName;
    private int receivedQuantity;
    private String requiredDate;
    private String dueDate;
    private String status;
}
