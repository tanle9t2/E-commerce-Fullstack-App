package com.tanle.e_commerce.utils.specification;

import com.tanle.e_commerce.entities.Product;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class ProductSpes {

    public static Specification<Product> getProductDetail(String optionsValue) {
        return ((root, query, criteriaBuilder) -> {
            return null;
        });
    }
}
