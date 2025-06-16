package com.moocafe.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HeadPurchaseItemDto {
    private Integer id;
    private String itemCode;
    private String itemName;
    private int receivedQuantity;
    private LocalDate dueDate;
    private LocalDate expirationDate;
    private String supplier;
    private String status;
}