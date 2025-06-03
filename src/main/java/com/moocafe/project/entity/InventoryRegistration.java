package com.moocafe.project.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Table(name="inventoryRegistration")
@Getter
@Setter
@NoArgsConstructor
public class InventoryRegistration {
    @Id
    @Column(length = 50)
    private String itemCode;

    private String itemName;
    private String itemGroup;
    private String itemStandard;
    private int itemQuantity;
    private int itemPrice;
    private String itemClass;
    private Date expirationDate;
    private Date itemDate;
}
