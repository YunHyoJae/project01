package com.moocafe.project.dto;

import com.moocafe.project.entity.InventoryItem;
import com.moocafe.project.entity.PurchaseItem;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;


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
