package com.tanle.e_commerce.entities;

import com.tanle.e_commerce.entities.CompositeKey.OrderDetailKey;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "order_detail")
@Entity
public class OrderDetail {
    @EmbeddedId
    private OrderDetailKey id;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @MapsId("skuId")
    @JoinColumn(name = "sku_id")
    private SKU sku;

    @Column(name = "quantity")
    private int quantity;


}
