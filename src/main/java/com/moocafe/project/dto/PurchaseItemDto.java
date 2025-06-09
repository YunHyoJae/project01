package com.moocafe.project.dto;


import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PurchaseItemDto {
    private String itemName;
    private String itemCode;
    private String supplier;
    private int receivedQuantity;
    private LocalDate dueDate;
    private LocalDate expirationDate;
    private String status;
}
