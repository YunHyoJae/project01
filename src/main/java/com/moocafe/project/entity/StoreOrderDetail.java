package com.moocafe.project.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "StoreOrderDetail")
@IdClass(StoreOrderDetailId.class)
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreOrderDetail {
    @Id
    private Long orderId;

    @Id
    @Column(length = 50)
    private String itemCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderId", insertable = false, updatable = false)
    private StoreOrder storeOrder;

    private  int orderedQuantity;

    @Column(length = 50)
    private String status = "주문완료"; // 주문완료/준비중/배송중
}
