package com.tanle.e_commerce.mapper.decoratormapper;

import com.tanle.e_commerce.Repository.Jpa.ProductRepository;
import com.tanle.e_commerce.Repository.Jpa.TenantRepository;
import com.tanle.e_commerce.dto.CategoryDTO;
import com.tanle.e_commerce.entities.Category;
import com.tanle.e_commerce.entities.Product;
import com.tanle.e_commerce.entities.Tenant;
import com.tanle.e_commerce.exception.ResourceNotFoundExeption;
import com.tanle.e_commerce.mapper.CategoryMapper;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@NoArgsConstructor
public abstract class CategoryMapperDecorator implements CategoryMapper {

    @Autowired
    private CategoryMapper delegate;
    @Autowired
    private TenantRepository tenantRepository;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public Category convertEntity(CategoryDTO categoryDTO) {
        Category category = delegate.convertEntity(categoryDTO);

        Tenant tenant = tenantRepository.findById(categoryDTO.getTenantId())
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found tenant"));
        category.setTenant(tenant);

        if(categoryDTO.getProductsId() != null) {
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

}
