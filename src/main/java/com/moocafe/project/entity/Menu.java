package com.moocafe.project.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "Menu")
@Getter
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "menu_seq_gen")
    @SequenceGenerator(name = "menu_seq_gen", sequenceName = "menu_seq", allocationSize = 1)
    private Long id;

    @Column(length = 50)
    private String menuId;

    @Column(length = 50)
    private String menuName;

    @Column(length = 50)
    private String itemCode;

    private int quantityUsed;

    protected Menu() {}

    public Menu(String menuId, String menuName, String itemCode, int quantityUsed) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.itemCode = itemCode;
        this.quantityUsed = quantityUsed;
    }
}
