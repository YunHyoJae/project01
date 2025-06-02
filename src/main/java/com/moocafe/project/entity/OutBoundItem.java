package com.moocafe.project.entity;

import jakarta.persistence.*;

@Entity
public class OutBoundItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ItemId;

    @ManyToOne
    @JoinColumn(name = "OutBoundId")
    private OutBound outBound;

    private String ItemCode;
    private int ReceivedQuantity;
}
