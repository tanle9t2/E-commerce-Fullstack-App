package com.tanle.e_commerce.entities;


import jakarta.persistence.*;

@Entity
@Table(name = "activity_type")
public class ActivityType {
    @Id
    @Column(name = "activity_type_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
}
