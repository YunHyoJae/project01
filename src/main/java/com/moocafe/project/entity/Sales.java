package com.moocafe.project.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Date;

@Entity
@Table(name = "Sales")
@Getter
public class Sales {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sales_seq_gen")
    @SequenceGenerator(name = "sales_seq_gen", sequenceName = "sales_seq", allocationSize = 1)
    private Long saleId;

    private Integer storeId;

    @Column(length = 50)
    private String menuId;

    @Column(length = 50)
    private String menuName;

    private Integer quantity;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "saleTime")
    private Date saleTime;

    protected Sales() {}

    public Sales(Integer storeId, String menuId, String menuName, Integer quantity) {
        this.storeId = storeId;
        this.menuId = menuId;
        this.menuName = menuName;
        this.quantity = quantity;
    }
}
