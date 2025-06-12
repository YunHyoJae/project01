package com.moocafe.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SalesSummaryDto {

    private Integer storeId;
    private String storeName;
    private String menuId;
    private String menuName;
    private BigDecimal totalQuantity;
    private BigDecimal totalAmount;
}