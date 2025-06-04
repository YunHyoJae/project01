package com.moocafe.project.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Purchase {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String purchaseNumber;

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime orderedDate;

    @Column(nullable = false)
    private String typeOrder = "발주";

    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PurchaseItem> items;
}
