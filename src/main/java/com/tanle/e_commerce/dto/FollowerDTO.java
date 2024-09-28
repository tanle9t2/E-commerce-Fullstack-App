package com.tanle.e_commerce.dto;

import com.tanle.e_commerce.entities.User;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class FollowerDTO {
    private int followingId;
    private int followerId;
    private LocalDateTime followDate;
    private LocalDateTime unfollowDate;
}
