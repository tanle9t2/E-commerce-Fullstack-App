package com.tanle.e_commerce.service;

import com.tanle.e_commerce.dto.SKUDTO;
import com.tanle.e_commerce.entities.Option;
import com.tanle.e_commerce.entities.Product;
import com.tanle.e_commerce.entities.SKU;
import com.tanle.e_commerce.payload.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface SKUService {
    List<SKUDTO> createSKU(List<SKUDTO> skudtos, Product product);

    List<SKUDTO> updateSKU(Integer productId, List<SKUDTO> skudtos);
}
