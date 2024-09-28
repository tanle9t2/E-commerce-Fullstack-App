package com.tanle.e_commerce.entities;

import com.tanle.e_commerce.entities.CompositeKey.UserRoleKey;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_has_role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserHasRole {
    @EmbeddedId
    private UserRoleKey id;

    @MapsId("userId")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @MapsId("roleId")
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "grantor_id")
    private User grantor;
}
