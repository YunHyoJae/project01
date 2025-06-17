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
    private String saleTime;

    public SalesSummaryDto() {}

    // 반드시 Number로!
    public SalesSummaryDto(Integer storeId, String storeName, String menuId, String menuName,
                           Number totalQuantity, Number totalAmount, String saleTime) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.menuId = menuId;
        this.menuName = menuName;
        this.totalQuantity = totalQuantity == null ? null : new BigDecimal(totalQuantity.toString());
        this.totalAmount = totalAmount == null ? null : new BigDecimal(totalAmount.toString());
        this.saleTime = saleTime;
    }

//    6개짜리 생성자
    public SalesSummaryDto(Integer storeId, String storeName, String menuId, String menuName,
                           Number totalQuantity, Number totalAmount) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.menuId = menuId;
        this.menuName = menuName;
        this.totalQuantity = totalQuantity == null ? null : new BigDecimal(totalQuantity.toString());
        this.totalAmount = totalAmount == null ? null : new BigDecimal(totalAmount.toString());
        this.saleTime = null;
    }
}