package com.tanle.e_commerce.service;

import com.tanle.e_commerce.dto.CategoryDTO;
import com.tanle.e_commerce.respone.CategoryFilterResponse;
import com.tanle.e_commerce.respone.MessageResponse;
import com.tanle.e_commerce.respone.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface CategoryService {
    PageResponse<CategoryDTO> findAll(Pageable pageable);

    CategoryDTO findById(Integer id);

    List<CategoryDTO> findByTenantId(Integer id);
    List<CategoryDTO> getSubCategoryFollowLevel(int parentId, int tenantId, int level);

    CategoryDTO createCategory(Integer parent, CategoryDTO categoryDTO);

    MessageResponse deleteCategory(Integer parentId);

    String getSinglePath(int categoryId);

    List<CategoryDTO> getSubCategory(String name);

    Map<String, Object> addProductToCategory(Integer categoryId, List<Integer> productList);

    Map<String, Object> removeProductFromCategory(Integer categoryId, List<Integer> productList);

    CategoryDTO update(int categoryId, CategoryDTO categoryDTO);

    List<CategoryFilterResponse> getCategoryFollowLevel(int categoryId, int level);


}
