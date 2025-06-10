package com.moocafe.project.dto;

public class SimpleMenuDto {
    private String menuId;
    private String menuName;

    public SimpleMenuDto(String menuId, String menuName) {
        this.menuId = menuId;
        this.menuName = menuName;
    }

    public String getMenuId() { return menuId; }
    public String getMenuName() { return menuName; }
}