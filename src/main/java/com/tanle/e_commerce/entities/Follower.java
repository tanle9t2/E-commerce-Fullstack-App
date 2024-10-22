package com.tanle.e_commerce.entities;

import com.tanle.e_commerce.dto.FollowerDTO;
import com.tanle.e_commerce.entities.CompositeKey.FollowerKey;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "follower")
public class Follower {
    @EmbeddedId
    private FollowerKey followerKey;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "following_id")
    private MyUser following;

    @ManyToOne
    @MapsId("followerId")
    @JoinColumn(name = "user_id")
    private MyUser follower;


    @Column(name = "follow_date", insertable=false, updatable=false)
    private LocalDateTime followDate;

    @Column(name = "unfollow_date")
    private LocalDateTime unfollowDate;

    public FollowerDTO converDTO() {
        return FollowerDTO.builder()
                .followingId(following.getId())
                .followerId(follower.getId())
                .followDate(this.followDate)
                .unfollowDate(this.unfollowDate)
                .build();
    }
}
