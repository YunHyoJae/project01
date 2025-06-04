package com.moocafe.project.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OutBoundDto {
    private Long StoreId;
    private List<OutBoundItemDto> items;
}
