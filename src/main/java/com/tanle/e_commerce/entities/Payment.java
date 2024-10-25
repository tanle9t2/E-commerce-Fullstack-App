package com.tanle.e_commerce.entities;

import com.tanle.e_commerce.entities.enums.StatusPayment;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    @Id
    @Column(name = "payment_id")
    private int id;
    @Column(name = "payment_amount")
    private double amount;
    @Column(name = "payment_date")
    private LocalDateTime createdAt;
    @Column(name = "payment_status")
    @Enumerated(EnumType.STRING)
    private StatusPayment statusPayment;
    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
