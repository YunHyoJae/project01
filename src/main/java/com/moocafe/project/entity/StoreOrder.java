package com.moocafe.project.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Table(name = "StoreOrder")
@Getter
@Setter
@NoArgsConstructor
public class StoreOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String OrderNumber;

    @ManyToOne
    @JoinColumn(name = "StoreId", nullable = false)
    private Store store;

    @Temporal(TemporalType.DATE)
    private Date OrderDate = new Date();
}
