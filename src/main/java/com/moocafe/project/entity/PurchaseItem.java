package com.moocafe.project.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@NoArgsConstructor(access= AccessLevel.PROTECTED )
@AllArgsConstructor
@Builder
@Getter
public class PurchaseItem {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    @SequenceGenerator(name = "purchase_item_seq_gen", sequenceName = "purchase_item_seq", allocationSize = 1)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itemCode", referencedColumnName = "itemCode", nullable = false)
    private InventoryItem itemCode;

    @Column(nullable = false)
    private int receivedQuantity;

    @Column(nullable = false)
    private Date dueDate;

    @Column
    private Date expirationDate;

    @Column(nullable = false)
    private String supplier;

    @Column(nullable = false)
    @Setter
    private String status = "진행중";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchaseNumber", referencedColumnName = "purchaseNumber", nullable = false)
    private Purchase purchaseNumber;
}
