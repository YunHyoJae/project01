package com.moocafe.project.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Date;

@Entity
@Table(name = "inventoryStore")
@Getter
public class InventoryStore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String itemCode;

    private Integer storeId;
    private Integer count;

    @Temporal(TemporalType.DATE)
    private Date regDate;

    @Temporal(TemporalType.DATE)
    private Date moveDate;

    @Temporal(TemporalType.DATE)
    private Date modifyDate;

    protected InventoryStore() {}

    public InventoryStore(String itemCode, Integer storeId, Integer count, Date regDate) {
        this.itemCode = itemCode;
        this.storeId = storeId;
        this.count = count;
        this.regDate = regDate;
    }


    public void updateCount(Integer newCount, Date modifyDate) {
        this.count = newCount;
        this.modifyDate = modifyDate;
    }

    public void moveTo(Integer toStoreId, Date moveDate) {
        this.storeId = toStoreId;
        this.moveDate = moveDate;
    }

}