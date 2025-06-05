package com.moocafe.project.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoreOrderDetailDto {
    private String itemCode;
    private int orderedQuantity;
}
