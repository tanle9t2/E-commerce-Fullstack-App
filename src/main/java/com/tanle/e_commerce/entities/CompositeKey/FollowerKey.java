package com.tanle.e_commerce.entities.CompositeKey;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FollowerKey implements Serializable {
    @Column(name = "user_id")
    private int userId;
    @Column(name = "following_id")
    private int followerId;
    @Column(name = "follow_date")
    private LocalDateTime followDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FollowerKey that = (FollowerKey) o;
        return userId == that.userId && followerId == that.followerId && Objects.equals(followDate, that.followDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, followerId, followDate);
    }
}
