package com.moocafe.project.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InventorySummaryDto {
    private String itemCode;
    private String itemName;
    private Integer storeId;
    private java.math.BigDecimal totalCount;

    @SqlResultSetMapping(
            name = "InventorySummaryMapping",
            classes = @ConstructorResult(
                    targetClass = InventorySummaryDto.class,
                    columns = {
                            @ColumnResult(name = "itemCode", type = String.class),
                            @ColumnResult(name = "itemName", type = String.class),
                            @ColumnResult(name = "storeId", type = Integer.class),
                            @ColumnResult(name = "totalCount", type = Integer.class)
                    }
            )
    )
    @Entity
    class DummyMappingEntity {
        @Id
        private Long id; // 반드시 필요 없음, 단지 매핑용
    }
}
