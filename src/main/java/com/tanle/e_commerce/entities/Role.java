package com.tanle.e_commerce.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {
    @Id
    @Column(name = "role_id")
    private int id;

    @Column(name = "role_name")
    private String roleName;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Override
    public String toString() {
        return roleName + ", ";

    }
}
