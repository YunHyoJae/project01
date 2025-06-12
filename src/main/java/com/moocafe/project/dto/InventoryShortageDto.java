package com.moocafe.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InventoryShortageDto {
    private String itemName;
    private String storeName;
    private int stock;
}