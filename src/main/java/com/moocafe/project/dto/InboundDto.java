package com.moocafe.project.dto;

import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InboundDto {
    private String number;
    private String typeOrder;
    private String itemCode;
    private String itemName;
    private int receivedQuantity;
    private String status;
}