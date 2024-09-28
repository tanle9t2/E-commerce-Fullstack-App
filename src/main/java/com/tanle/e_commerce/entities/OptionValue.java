package com.tanle.e_commerce.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Table(name = "option_value")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OptionValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_value_id")
    private int id;
    @Column(name = "value_name")
    private String name;
}
