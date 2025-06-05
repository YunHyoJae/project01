package com.moocafe.project.dto;

import lombok.Data;

import java.util.Date;

@Data
public class InventoryStoreDto {
    private Long id;
    private String itemCode;
    private Integer storeId;
    private Integer count;
    private Date regDate;
    private Date moveDate;
    private Date modifyDate;
}
