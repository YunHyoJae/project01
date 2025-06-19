package com.moocafe.project.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class StoreOrderListResponseDto {
    private String orderNumber;
    private String orderDate;
    private String itemCode;
    private String itemName;
    private int orderedQuantity;
    private String status;
    private String returnStatus;

    public StoreOrderListResponseDto(String orderNumber, String orderDate, String itemCode,
                                     String itemName, int orderedQuantity, String status,
                                     String returnStatus) {
        this.orderNumber = orderNumber;
        this.orderDate = orderDate;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.orderedQuantity = orderedQuantity;
        this.status = status;
        this.returnStatus = returnStatus;
    }


}
