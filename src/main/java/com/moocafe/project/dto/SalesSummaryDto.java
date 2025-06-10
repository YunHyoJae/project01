package com.moocafe.project.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter

public class SalesSummaryDto {

    private Integer storeId;
    private String storeName;
    private String menuId;
    private String menuName;
    private BigDecimal totalQuantity;
    private BigDecimal totalAmount;

    public SalesSummaryDto(
            Integer storeId,
            String storeName,
            String menuId,
            String menuName,
            BigDecimal totalQuantity,
            BigDecimal totalAmount
    ) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.menuId = menuId;
        this.menuName = menuName;
        this.totalQuantity = totalQuantity;
        this.totalAmount = totalAmount;
    }
}