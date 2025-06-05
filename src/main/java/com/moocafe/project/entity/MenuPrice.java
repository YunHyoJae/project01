package com.moocafe.project.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "MenuPrice")
@Getter
public class MenuPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "menu_price_seq_gen")
    @SequenceGenerator(name = "menu_price_seq_gen", sequenceName = "menu_price_seq", allocationSize = 1)
    private Long id;

    @Column(length = 50, unique = true)
    private String menuId;

    @Column(nullable = false)
    private Integer menuPrice;

    protected MenuPrice() {}

    public MenuPrice(String menuId, Integer menuPrice) {
        this.menuId = menuId;
        this.menuPrice = menuPrice;
    }
}
