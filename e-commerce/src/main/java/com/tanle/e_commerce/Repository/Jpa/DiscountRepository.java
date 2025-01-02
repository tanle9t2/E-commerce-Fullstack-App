package com.tanle.e_commerce.Repository.Jpa;

import com.tanle.e_commerce.entities.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiscountRepository extends JpaRepository<Discount,Integer> {
    @Query("from Discount where product.id = ?1")
    List<Discount> findByProduct(Integer productId);
}
