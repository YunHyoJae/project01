package com.moocafe.project.dto;

import lombok.*;

@Getter
@Builder
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ItemQuantityDto {
    private String itemCode;
    private Long returnQuantity;
}

