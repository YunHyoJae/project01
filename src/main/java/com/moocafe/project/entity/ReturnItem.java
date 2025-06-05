package com.moocafe.project.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ReturnItem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "returnNumber", referencedColumnName = "returnNumber", nullable = false)
    private Return returnEntity;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itemCode", referencedColumnName = "itemCode", nullable = false)
    private InventoryStore itemCode;

    @Column(nullable = false)
    private int returnQuantity;

    @Column(nullable = false)
    private String status = "진행중";
}
