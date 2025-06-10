package com.moocafe.project.dto;

public class MenuDto {
    private String menuId;
    private String menuName;
    private String itemCode;
    private int quantityUsed;

    // 기본 생성자
    public MenuDto() {}

    // 전체 생성자
    public MenuDto(String menuId, String menuName, String itemCode, int quantityUsed) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.itemCode = itemCode;
        this.quantityUsed = quantityUsed;
    }

    public String getMenuId() { return menuId; }
    public void setMenuId(String menuId) { this.menuId = menuId; }

    public String getMenuName() { return menuName; }
    public void setMenuName(String menuName) { this.menuName = menuName; }

    public String getItemCode() { return itemCode; }
    public void setItemCode(String itemCode) { this.itemCode = itemCode; }

    public int getQuantityUsed() { return quantityUsed; }
    public void setQuantityUsed(int quantityUsed) { this.quantityUsed = quantityUsed; }
}