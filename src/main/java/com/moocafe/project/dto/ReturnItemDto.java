package com.moocafe.project.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ReturnItemDto {
    private int id;
    private String returnNumber;
    private String itemCode;
    private int returnQuantity;
    private String status;
}
