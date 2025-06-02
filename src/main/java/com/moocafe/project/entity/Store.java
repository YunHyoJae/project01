package com.moocafe.project.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name="store")
@Getter
@Setter
@NoArgsConstructor
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private Long userId;
    private String Name;
    private String storeNumber;
    private String space;
    private String tel;
    private String zipcode;
    private String address01;
    private String address02;
    private Date regDate;
    private Date modifyDate;
    private String state;
}
