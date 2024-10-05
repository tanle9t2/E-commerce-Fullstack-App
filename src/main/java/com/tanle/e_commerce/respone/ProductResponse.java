package com.tanle.e_commerce.respone;

import com.tanle.e_commerce.dto.ProductDTO;
import com.tanle.e_commerce.dto.SKUDTO;
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