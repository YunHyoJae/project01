package com.moocafe.project.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name="franchiseBoard")
public class FranchiseBoard extends BaseEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Integer id;
}
