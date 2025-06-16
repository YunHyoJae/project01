package com.moocafe.project.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {
    private String itemCode;
    private String itemName;
    private int needed;
    private int stock;
    private int price;
    // getters/setters, 생성자
    public OrderItemDto(String itemCode, int needed) {
        this.itemCode = itemCode;
        this.needed = needed;
    }
}
