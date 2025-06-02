package com.moocafe.project.entity;

import jakarta.persistence.*;
import org.apache.catalina.Store;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class StoreOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String OrderNumber;

    @ManyToOne
    @JoinColumn(name = "StoreId", nullable = false)
    private Store store;

    @Column(nullable = false)
    private LocalDate OrderDate = LocalDate.now();

    @OneToMany(mappedBy = "StoreOrder", cascade = CascadeType.ALL)
    private List<StoreOrderDetail> OrderDetails = new ArrayList<>();
}
