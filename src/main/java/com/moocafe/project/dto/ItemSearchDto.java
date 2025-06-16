package com.moocafe.project.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemSearchDto {
    private String itemCode;
    private String itemName;
    private int itemPrice;
    private int stockQuantity; // ✅ 본사 기준 재고
}
