package com.moocafe.project.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoreOrderDto {
    private String orderNumber;
    private Integer storeId;
    private List<StoreOrderDetailDto> items;
}
