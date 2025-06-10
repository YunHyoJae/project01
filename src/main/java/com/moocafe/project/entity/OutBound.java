package com.moocafe.project.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Table(name = "OutBound")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutBound {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long outBoundId;

    private Integer storeId;

    @Temporal(TemporalType.DATE)
    private Date requiredDate = new Date();

    private char approved = 'N';

    @Temporal(TemporalType.DATE)
    private Date approvedDate;

    @Temporal(TemporalType.DATE)
    private Date dueDate;

    @Column(length = 50)
    private String status; // 준비중/출고완료
}
