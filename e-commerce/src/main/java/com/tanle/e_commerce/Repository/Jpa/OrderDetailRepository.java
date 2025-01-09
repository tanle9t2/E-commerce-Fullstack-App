package com.tanle.e_commerce.Repository.Jpa;

import com.tanle.e_commerce.entities.CompositeKey.OrderDetailKey;
import com.tanle.e_commerce.entities.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, OrderDetailKey> {

    @Query("""
                select sum(od.quantity)from OrderDetail od where od.sku.product.id =:productId
            """)
    Optional<Integer> sumProductSell(@Param("productId") int productId);
}
