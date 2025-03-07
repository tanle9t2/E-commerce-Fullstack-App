package com.tanle.e_commerce.respone;

import com.tanle.e_commerce.dto.CategoryDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryFilterResponse {
    private CategoryDTO parent;
    private List<CategoryDTO> subCategory;
}
