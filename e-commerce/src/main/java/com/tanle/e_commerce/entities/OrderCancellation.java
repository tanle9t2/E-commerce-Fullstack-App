package com.tanle.e_commerce.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orders_cancellation")
public class OrderCancellation {

    @Id
    @Column(name = "order_id")
    private int id;
    @Column(name = "canceled_at")
    private LocalDateTime canceledAt;
    @Column(name = "reason")
    private String reason;
    @Column(name = "operation")
    private String operation;

    @MapsId
    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

//    public OrderCancellation(Order order, LocalDateTime canceledAt, String reason, String operation) {
//        super(order.getId(), order.getCreatedAt(), order.getStatus()
//                , order.getUser(), order.getTenant()
//                , order.getAddress(), order.getOrderDetails());
//        this.canceledAt = canceledAt;
//        this.reason = reason;
//        this.operation = operation;
//    }

}
