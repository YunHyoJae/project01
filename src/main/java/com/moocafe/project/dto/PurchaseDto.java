package com.moocafe.project.dto;

import com.moocafe.project.entity.Purchase;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseDto {
    private String purchaseNumber;
    private LocalDate orderedDate;
    private List<PurchaseItemDto> items;
}
