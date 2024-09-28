package com.tanle.e_commerce.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "token")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_token")
    private int id;
    @Column(name = "token")
    private String token;
    @Column(name = "token_type")
    private String tokenType;
    @Column(name = "expired")
    private boolean expired;
    @Column(name = "revoked")
    private boolean revoked;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User myUser;
}
