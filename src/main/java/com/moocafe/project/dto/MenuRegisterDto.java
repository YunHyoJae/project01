package com.moocafe.project.dto;

import java.util.List;


public class MenuRegisterDto {
    private String menuId;
    private String menuName;
    private int menuPrice;
    private List<MenuDto> menuItems;

    // Getter & Setter
    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public int getMenuPrice() {
        return menuPrice;
    }

    public void setMenuPrice(int menuPrice) {
        this.menuPrice = menuPrice;
    }

    public List<MenuDto> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuDto> menuItems) {
        this.menuItems = menuItems;
    }
}