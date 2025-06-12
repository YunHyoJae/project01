package com.moocafe.project.dto;

import com.moocafe.project.entity.InventoryItem;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ReturnItemDto {
    private int id;
    private String returnNumber;
    private String itemCode;
    private String itemName;
    private String orderNumber;
    private int returnQuantity;
    private String status;
}