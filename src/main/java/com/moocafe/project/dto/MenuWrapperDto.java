package com.moocafe.project.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MenuWrapperDto {

    private String menuId;
    private String menuName;
    private int menuPrice;

    private List<MenuItemDto> menuItems;

    // 내부 품목 DTO
    @Getter
    @Setter
    public static class MenuItemDto {
        private String itemCode;
        private int quantityUsed;
    }
}