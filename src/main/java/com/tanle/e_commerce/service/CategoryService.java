package com.tanle.e_commerce.service;

import com.tanle.e_commerce.dto.CategoryDTO;
import com.tanle.e_commerce.entities.Category;
import com.tanle.e_commerce.entities.Product;
import com.tanle.e_commerce.payload.ApiResponse;
import com.tanle.e_commerce.payload.MessageResponse;
import com.tanle.e_commerce.payload.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface CategoryService {
    PageResponse<CategoryDTO> findAll(Pageable pageable);

    CategoryDTO findById(Integer id);

    CategoryDTO createCategory(Integer parent, CategoryDTO categoryDTO);

    MessageResponse deleteCategory(Integer parentId);


    PageResponse<CategoryDTO> getSubCategory(String name, Pageable pageable);

    Map<String,Object> addProductToCategory(Integer categoryId, List<Integer> productList);

    Map<String, Object> removeProductFromCategory(Integer categoryId, List<Integer> productList);

    CategoryDTO update(int categoryId, CategoryDTO categoryDTO);


}
