package com.tanle.e_commerce.dto;

import com.tanle.e_commerce.respone.PageResponse;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class TenantDTO {
    private int id;
    private String name;
    private Date createdAt;
    private Integer pickupAddressId;
    private Integer returnAddressId;
    private String phoneNumber;
    private String email;
    private boolean isActive;
    private int userId;
    private long totalProduct;
    private long follower;
    private long following;
    private double avgRating;
    private String tenantImage;
    private Long totalComment;
}
