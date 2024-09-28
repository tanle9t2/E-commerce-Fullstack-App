package com.tanle.e_commerce.entities.CompositeKey;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderDetailKey implements Serializable {

    @Column(name = "order_id")
    private int orderId;
    @Column(name = "sku_id")
    private int skuId;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDetailKey that = (OrderDetailKey) o;
        return orderId == that.orderId && skuId == that.skuId && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, skuId, createdAt);
    }
}
