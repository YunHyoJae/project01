package com.moocafe.project.dto;

import com.moocafe.project.entity.InventoryItem;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ReturnItemDto {
    private String itemCode;
    private String itemName;
    private int returnQuantity;
    private String requiredDate;
}