package com.tanle.e_commerce.specification;

import com.tanle.e_commerce.entities.Product;
import com.tanle.e_commerce.entities.SKU;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {
    public static Specification<Product> withOwnerId(int tenantId, String keyword, String minPrice, String maxPrice) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true); // Ensure distinct products
            Predicate predicate = criteriaBuilder.equal(root.get("tenant").get("id"), tenantId);
            if (keyword != null && !keyword.isEmpty()) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + keyword.toLowerCase() + "%"));
            }
            if (minPrice != null || maxPrice != null) {
                Join<Product, SKU> priceJoin = root.join("skus");
                if (minPrice != null) {
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(priceJoin.get("price"), Double.parseDouble(minPrice)));
                }
                if (maxPrice != null) {
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(priceJoin.get("price"), Double.parseDouble(maxPrice)));
                }
            }
            return predicate;
        };
    }
}
