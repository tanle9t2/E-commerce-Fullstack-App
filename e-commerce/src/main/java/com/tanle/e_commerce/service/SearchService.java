package com.tanle.e_commerce.service;

import com.tanle.e_commerce.dto.ProductDTO;
import com.tanle.e_commerce.entities.Product;
import com.tanle.e_commerce.respone.FilterSearchResponse;
import com.tanle.e_commerce.respone.PageResponse;
import com.tanle.e_commerce.respone.PageSearchResponse;

import java.util.List;
import java.util.Map;

public interface SearchService {
    PageResponse<Product> findBySpecs(Map<String, String> filter, int page, int size);

    PageSearchResponse searchProduct(Map<String, String> condition, int page, int size);

    List<FilterSearchResponse> getFilterSearch(Map<String, String> condition);

    Map<Integer, String> searchHint(String keyword);

    ProductDTO test(String id);
}
