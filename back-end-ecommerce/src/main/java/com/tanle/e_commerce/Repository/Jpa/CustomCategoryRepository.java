package com.tanle.e_commerce.Repository.Jpa;

import com.tanle.e_commerce.dto.CategoryDTO;
import com.tanle.e_commerce.entities.Category;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CustomCategoryRepository {
    Category createCategory(Category parent, Category category);

    void updateIndexDeleteCategory(int left, int right);
}
