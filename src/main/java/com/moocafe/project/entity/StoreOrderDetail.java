package com.moocafe.project.entity;

import jakarta.persistence.*;

@Entity
@IdClass(StoreOrderDetailId.class)
public class StoreOrderDetail {
    @Id
    private Long orderId;

    @Id
    private String ItemCode;

    @ManyToOne
    @JoinColumn(name = "orderId", insertable = false, updatable = false)
    private StoreOrder storeOrder;

    private String status = "주문완료"; // 주문완료/준비중/배송중

    private  int OrderedQuantity;
}
