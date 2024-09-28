package com.tanle.e_commerce.service;

import com.tanle.e_commerce.dto.ProductDTO;
import com.tanle.e_commerce.entities.Product;
import com.tanle.e_commerce.payload.PageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Map;

public interface SearchService {
    PageResponse<Product> findBySpecs(Map<String,String> filter, int page, int size);
    PageResponse<ProductDTO> searchProduct(Map<String,String> condition, int page,int size);
    ProductDTO test(String id);
}
