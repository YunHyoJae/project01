package com.moocafe.project.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OutBoundListResponseDto {
    private Integer outBoundId;
    private String storeName;
    private String itemCode;
    private String itemName;
    private Integer receivedQuantity;
    private String requiredDate;
    private String dueDate;
    private String status;
}
