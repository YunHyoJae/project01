package com.moocafe.project.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "OutBoundItem")
@Getter
@Setter
@NoArgsConstructor
public class OutBoundItem {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long ItemId;

    @ManyToOne
    @JoinColumn(name = "OutBoundId", nullable = false)
    private OutBound outBound;

    @Column(nullable = false, length = 50)
    private String ItemCode;

    private int ReceivedQuantity;
}
