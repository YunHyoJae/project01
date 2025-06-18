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
@NoArgsConstructor(access= AccessLevel.PROTECTED )
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Return {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "return_seq_gen", sequenceName = "RETURN_SEQ", allocationSize = 1)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String returnNumber;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderNumber", referencedColumnName = "orderNumber", nullable = false)
    private StoreOrder orderNumber;

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime requiredDate;

    @Column(nullable = false)
    private String returnNote;

    @Column(nullable = false)
    private String typeReturn = "반품";

    @OneToMany(mappedBy = "returnEntity")
    private List<ReturnItem> items;
}
