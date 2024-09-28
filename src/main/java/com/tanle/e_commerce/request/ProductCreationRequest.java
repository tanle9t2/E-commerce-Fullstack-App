package com.tanle.e_commerce.request;

import com.tanle.e_commerce.dto.SKUDTO;
import com.tanle.e_commerce.entities.Option;
import com.tanle.e_commerce.entities.Product;
import com.tanle.e_commerce.entities.SKU;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class ProductCreationRequest {
    private Product product;
    private List<Option> options;
    private List<SKUDTO> skus;
}
