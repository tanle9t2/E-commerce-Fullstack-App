package com.tanle.e_commerce.specification;

import com.tanle.e_commerce.entities.Product;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {
    public static Specification<Product> withOwnerId(int tenantId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), tenantId);
    }

}
