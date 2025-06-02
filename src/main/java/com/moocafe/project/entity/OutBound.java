package com.moocafe.project.entity;

import jakarta.persistence.*;
import org.apache.catalina.Store;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class OutBound {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long OutBoundId;

    @ManyToOne
    @JoinColumn(name = "StoreId", nullable = false)
    private Store store;

    private LocalDate requiredDate = LocalDate.now();
    private LocalDate approvedDate;
    private LocalDate dueDate;

    private String status; // 준비중/출고완료

    private char approved = 'N';

    @OneToMany(mappedBy = "OutBound", cascade = CascadeType.ALL)
    private List<OutBoundItem> OutBoundDetails = new ArrayList<>();
}
