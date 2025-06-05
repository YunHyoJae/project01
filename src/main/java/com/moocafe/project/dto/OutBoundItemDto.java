package com.moocafe.project.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OutBoundItemDto {
    private String itemCode;
    private int receivedQuantity;
}
