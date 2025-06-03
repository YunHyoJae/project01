package com.moocafe.project.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Table(name = "OutBound")
@Getter
@Setter
@NoArgsConstructor
public class OutBound {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long OutBoundId;

    @ManyToOne
    @JoinColumn(name = "StoreId", nullable = false)
    private Store store;

    @Temporal(TemporalType.DATE)
    private Date RequiredDate = new Date();

    private char Approved = 'N';

    @Temporal(TemporalType.DATE)
    private Date ApprovedDate;

    @Temporal(TemporalType.DATE)
    private Date DueDate;

    @Column(length = 50)
    private String status; // 준비중/출고완료
}
