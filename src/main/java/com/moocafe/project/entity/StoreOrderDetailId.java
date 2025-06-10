package com.moocafe.project.entity;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoreOrderDetailId implements Serializable {
    private Long orderId;
    private String itemCode;
}
