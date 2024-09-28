package com.tanle.e_commerce.entities.CompositeKey;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserRoleKey implements Serializable {
    @Column(name = "user_id")
    private int userId;
    @Column(name = "role_id")
    private int roleId;
    @Column(name = "created_at")
    private LocalDateTime createAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRoleKey that = (UserRoleKey) o;
        return userId == that.userId && roleId == that.roleId && Objects.equals(createAt, that.createAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, roleId, createAt);
    }
}
