package com.tanle.e_commerce.entities;


import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "activity")
public class Activity {
    @Id
    @Column(name = "activity_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "activity_time")
    private Date activityTime;

}
