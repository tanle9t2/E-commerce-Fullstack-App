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

    Page<Order> findOrderByTenant(Tenant tenant, Pageable pageable);

    @Query("""
            FROM Order o JOIN User u
            ON o.user.id = u.id 
            where u.id = ?1 
            and (?2 is null or o.status = ?2)
        """)
    List<Order> findOrderId(Integer userId, StatusOrder type);
}
