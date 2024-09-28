package com.tanle.e_commerce.payload;

import com.tanle.e_commerce.dto.ProductDTO;
import com.tanle.e_commerce.dto.SKUDTO;
import com.tanle.e_commerce.entities.Category;
import com.tanle.e_commerce.entities.Product;
import com.tanle.e_commerce.entities.SKU;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ProductResponse {
    private ProductDTO product;
    private List<SKUDTO> skus;
    private int status;
}