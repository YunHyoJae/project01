package com.moocafe.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MenuSimpleDto {
    private String menuId;
    private String menuName;
    private int menuPrice;
}