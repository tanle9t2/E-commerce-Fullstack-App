package com.tanle.e_commerce.specification;

import com.tanle.e_commerce.entities.Category;
import com.tanle.e_commerce.entities.Product;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class CategorySpecification {
    public static Specification<Category> getSinglePath(int categoryId) {
        return (root, query, criteriaBuilder) -> {
            Join<Category, Category> joinEntity = root.join("category");
            Predicate lftBetweenPredicate = criteriaBuilder
                    .between(root.get("lft"), joinEntity.get("lft"), joinEntity.get("rgt"));
            Predicate equalPredicate = criteriaBuilder.equal(root.get("product_category_id"), categoryId);

            return criteriaBuilder.and(lftBetweenPredicate, equalPredicate);
        };
    }
}
