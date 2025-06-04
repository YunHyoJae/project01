package com.moocafe.project.dto;

import jakarta.persistence.Column;

import java.util.Date;

public class InventoryItemDto {
    private String itemCode;
    private String itemName;
    private String itemGroup;
    private String itemStandard;
    private int itemQuantity;
    private int itemPrice;
    private String itemClass;
    private String expirationDate;
    @Column(name = "itemDate", insertable = false, updatable = false)
    private Date itemDate;


    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemGroup() {
        return itemGroup;
    }

    public void setItemGroup(String itemGroup) {
        this.itemGroup = itemGroup;
    }

    public String getItemStandard() {
        return itemStandard;
    }

    public void setItemStandard(String itemStandard) {
        this.itemStandard = itemStandard;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(int itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemClass() {
        return itemClass;
    }

    public void setItemClass(String itemClass) {
        this.itemClass = itemClass;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }
}
