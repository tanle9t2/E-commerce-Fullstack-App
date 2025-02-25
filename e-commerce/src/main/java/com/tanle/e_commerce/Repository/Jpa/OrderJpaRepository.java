package com.tanle.e_commerce.Repository.Jpa;

import com.tanle.e_commerce.entities.Order;
import com.tanle.e_commerce.entities.Tenant;
import com.tanle.e_commerce.entities.enums.StatusOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderJpaRepository extends JpaRepository<Order,Integer>, JpaSpecificationExecutor<Order>{

    @Query("""
        FROM Order o, OrderDetail  od, SKU s, Product  p, Tenant t
        WHERE o.id = od.order.id  
        AND s.id = od.sku.id
        AND p.id = s.product.id
        AND t.id = p.tenant.id
        AND t.id = ?1
    """)
    Page<Order> findOrderByTenant(int tenantId, Pageable pageable);

    @Query("""
            FROM Order o JOIN MyUser u
            ON o.myUser.id = u.id 
            where u.username = ?1 
            and (?2 is null or o.status = ?2)
        """)
    Page<Order> findOrderId(String username, StatusOrder type, Pageable pageable);
}
