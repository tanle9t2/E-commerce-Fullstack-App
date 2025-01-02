package com.tanle.e_commerce.dto;

import com.tanle.e_commerce.entities.CartItem;
import com.tanle.e_commerce.entities.CompositeKey.CartItemKey;
import com.tanle.e_commerce.entities.Product;
import com.tanle.e_commerce.entities.SKU;
import com.tanle.e_commerce.entities.Tenant;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class CartItemDTO {
    private int quantity;
    private int skuId;
    private String skuNo;
    private String modelName;
    private int stock;
    private double sellPrice;
    private LocalDateTime createAt;
    private List<SKUDTO> skus;
    private ProductDTO product;
}
