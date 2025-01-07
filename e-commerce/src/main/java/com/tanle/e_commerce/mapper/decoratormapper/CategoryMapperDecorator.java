package com.tanle.e_commerce.mapper.decoratormapper;

import com.tanle.e_commerce.Repository.Jpa.CategoryRepository;
import com.tanle.e_commerce.Repository.Jpa.ProductRepository;
import com.tanle.e_commerce.Repository.Jpa.TenantRepository;
import com.tanle.e_commerce.dto.CategoryDTO;
import com.tanle.e_commerce.entities.Category;
import com.tanle.e_commerce.entities.Product;
import com.tanle.e_commerce.entities.Tenant;
import com.tanle.e_commerce.exception.ResourceNotFoundExeption;
import com.tanle.e_commerce.mapper.CategoryMapper;
import com.tanle.e_commerce.service.CategoryService;
import com.tanle.e_commerce.specification.CategorySpecification;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public abstract class CategoryMapperDecorator implements CategoryMapper {

    @Autowired
    private CategoryMapper delegate;
    @Autowired
    private TenantRepository tenantRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category convertEntity(CategoryDTO categoryDTO) {
        Category category = delegate.convertEntity(categoryDTO);

        Tenant tenant = tenantRepository.findById(categoryDTO.getTenantId())
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found tenant"));
        category.setTenant(tenant);

        if (categoryDTO.getProductsId() != null) {
            for (Integer index : categoryDTO.getProductsId()) {
                Product product = tenant.getProducts().stream()
                        .filter(p -> p.getId() == index)
                        .findFirst()
                        .orElseThrow(() -> new ResourceNotFoundExeption("Not found product"));
                category.addProduct(product);
            }
        }

        return category;
    }

    @Override
    public CategoryDTO convertDTO(Category category) {
        CategoryDTO categoryDTO = delegate.convertDTO(category);
        List<String> categories = categoryRepository.getPath(categoryDTO.getId());
        categoryDTO.setPathCategory(categories);
        return categoryDTO;
    }
}
