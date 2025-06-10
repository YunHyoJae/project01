package com.moocafe.project.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "OutBoundItem")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutBoundItem {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long itemId;

    @ManyToOne
    @JoinColumn(name = "outBoundId", nullable = false)
    private OutBound outBound;

    @Column(nullable = false, length = 50)
    private String itemCode;

    private Integer receivedQuantity;
}
