package com.moocafe.project.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemSearchDto {
    private String itemCode;
    private String itemName;
    private int itemPrice;
}
