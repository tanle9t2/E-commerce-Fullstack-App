package com.tanle.e_commerce.entities.CompositeKey;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CartItemKey implements Serializable {
    @Column(name = "shopping_cart_id")
    private int shoppingCartId;
    @Column(name = "sku_id")
    private int skuId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItemKey that = (CartItemKey) o;
        return shoppingCartId == that.shoppingCartId && skuId == that.skuId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(shoppingCartId, skuId);
    }
}
