package com.tanle.e_commerce.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PaymentDTO {
    private int id;
    private double amount;
    private LocalDateTime createdAt;
    private String status;
    private int userId;
    private String email;
    private OrderDTO order;
}
