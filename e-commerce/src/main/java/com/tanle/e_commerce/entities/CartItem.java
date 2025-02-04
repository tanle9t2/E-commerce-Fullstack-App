package com.tanle.e_commerce.entities;

import com.tanle.e_commerce.dto.CartItemDTO;
import com.tanle.e_commerce.dto.ProductDTO;
import com.tanle.e_commerce.entities.CompositeKey.CartItemKey;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CartItem {
    @EmbeddedId
    private CartItemKey cartItemKey;
    @ManyToOne
    @MapsId("shoppingCartId")
    @JoinColumn(name = "shopping_cart_id")
    private Cart cart;
    @ManyToOne
    @MapsId("skuId")
    @JoinColumn(name = "sku_id")
    private SKU sku;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "create_at")
    private LocalDateTime createAt;

    public CartItemDTO converDTO() {
        return CartItemDTO.builder()
                .skuId(this.sku.getId())
                .skuNo(this.sku.getSkuNo())
                .modelName(sku.getModalName())
                .quantity(this.quantity)
                .createAt(this.createAt)
                .stock(this.sku.getStock())
                .sellPrice(this.sku.getPrice())
                .product(sku.getProduct().converDTO())
                .build();
    }

}
