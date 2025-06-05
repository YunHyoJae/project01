package com.moocafe.project.dto;

import com.moocafe.project.entity.PurchaseItem;
import lombok.*;

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
    private String expectedDate;
    private String orderDate;
    private String purchaseNumber;
    private List<PurchaseItem> items;

}
