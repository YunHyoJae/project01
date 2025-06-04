package com.moocafe.project.entity;


import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "inventoryRegistration")
@Getter
@EntityListeners(AuditingEntityListener.class)
public class InventoryItem {

    @Id
    @Column(length = 50)
    private String itemCode;

    @Column(length = 50)
    private String itemName;

    @Column(length = 50)
    private String itemGroup;

    @Column(length = 50)
    private String itemStandard;

    private Integer itemQuantity;

    private Integer itemPrice;

    @Column(length = 50)
    private String itemClass;

    @Temporal(TemporalType.DATE)
    private Date expirationDate;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime itemDate;


    protected InventoryItem() {}

    // ✅ 생성자 (필수값 세팅용)
    public InventoryItem(String itemCode, String itemName, String itemGroup,
                         String itemStandard, Integer itemQuantity, Integer itemPrice,
                         String itemClass, Date expirationDate) {
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.itemGroup = itemGroup;
        this.itemStandard = itemStandard;
        this.itemQuantity = itemQuantity;
        this.itemPrice = itemPrice;
        this.itemClass = itemClass;
        this.expirationDate = expirationDate;
    }

    // ✅ 필요한 경우 업데이트용 메서드
    public void updateItem(String itemName, String itemGroup,
                           String itemStandard, Integer itemQuantity,
                           Integer itemPrice, String itemClass, Date expirationDate) {
        this.itemName = itemName;
        this.itemGroup = itemGroup;
        this.itemStandard = itemStandard;
        this.itemQuantity = itemQuantity;
        this.itemPrice = itemPrice;
        this.itemClass = itemClass;
        this.expirationDate = expirationDate;
    }
}