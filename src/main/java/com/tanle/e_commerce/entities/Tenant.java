package com.tanle.e_commerce.entities;

import com.tanle.e_commerce.dto.TenantDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "tenant")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Tenant {
    @Id
    @Column(name = "tenant_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "domain")
    private String domain;
    @Column(name = "create_at")
    private LocalDateTime createAt;
    @OneToMany(mappedBy = "tenant")
    private List<Product> products;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    @OneToOne
    @JoinColumn(name = "pickup_address")
    private Address pickupAddress;
    @OneToOne
    @JoinColumn(name = "return_address")
    private Address returnAddress;
    @Column(name = "email")
    private String email;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "is_active")
    private boolean isActive;

    public TenantDTO converDTO() {
        return TenantDTO.builder()
                .id(this.id)
                .name(this.name)
                .creatAt(this.createAt)
                .userId(this.user.getId())
                .follower(this.user.getFollowers().stream()
                        .filter(f -> f.getUnfollowDate() == null)
                        .count())
                .following(this.user.getFollowing().stream()
                        .filter(f -> f.getUnfollowDate() == null)
                        .count())
                .build();
    }
}
