package com.tanle.e_commerce.specification;

import com.tanle.e_commerce.entities.Category;
import com.tanle.e_commerce.entities.Order;
import com.tanle.e_commerce.entities.OrderDetail;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class OrderSpecification {
    public static Specification<Category> countProductSell(int productId) {
        return (root, query, criteriaBuilder) -> {
            Join<Order, OrderDetail> joinEntity = root.join("orderDetails");

            Predicate equalPredicate = criteriaBuilder.equal(joinEntity.get("product_id"), productId);

            return criteriaBuilder.and(equalPredicate);
        };
    }
}
