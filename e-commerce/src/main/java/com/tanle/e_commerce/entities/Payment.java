package com.tanle.e_commerce.entities;

import com.tanle.e_commerce.entities.enums.StatusPayment;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private int id;
    @Column(name = "payment_amount")
    private double amount;
    @Column(name = "payment_date")
    @CreatedDate
    private LocalDateTime createdAt;
    @Column(name = "payment_status")
    @Enumerated(EnumType.STRING)
    private StatusPayment status;
    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

}
