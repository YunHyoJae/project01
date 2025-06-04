package com.moocafe.project.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private String OrderNumber;
    private Long StoreId;
    private List<OrderDetailDto> items;
}
