package com.tanle.e_commerce.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Table(name = "option_value")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OptionValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_value_id")
    private int id;
    @Column(name = "value_name")
    private String name;
}
