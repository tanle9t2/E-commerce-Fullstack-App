package com.tanle.e_commerce.service;

import com.tanle.e_commerce.dto.ProductDTO;
import com.tanle.e_commerce.entities.Product;
import com.tanle.e_commerce.respone.PageResponse;

import java.util.Map;

public interface SearchService {
    PageResponse<Product> findBySpecs(Map<String,String> filter, int page, int size);
    PageResponse<ProductDTO> searchProduct(Map<String,String> condition, int page,int size);
    ProductDTO test(String id);
}
