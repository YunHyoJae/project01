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
    @SequenceGenerator(name = "return_item_seq_gen", sequenceName = "return_item_seq", allocationSize = 1)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "returnNumber", referencedColumnName = "returnNumber", nullable = false)
    private Return returnEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itemCode", referencedColumnName = "itemCode", nullable = false)
    private InventoryItem item;


    @Column(nullable = false)
    private int returnQuantity;

    @Column(nullable = false)
    private String status = "진행중";
}
