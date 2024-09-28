package com.tanle.e_commerce.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class TenantDTO {
    private int id;
    private String name;
    private LocalDateTime creatAt;
    private int userId;
    private long totalProduct;
    private long follower;
    private long following;
    private double avgRating;
}
