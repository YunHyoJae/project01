package com.moocafe.project.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StoreOrderListResponseDto {
    private String orderNumber;
    private String orderDate;
    private String itemCode;
    private String itemName;
    private int orderedQuantity;
    private String status;
}
