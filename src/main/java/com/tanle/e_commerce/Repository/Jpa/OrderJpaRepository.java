package com.tanle.e_commerce.Repository.Jpa;

import com.tanle.e_commerce.entities.Order;
import com.tanle.e_commerce.entities.Tenant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderJpaRepository extends JpaRepository<Order,Integer>, JpaSpecificationExecutor<Order>{

    Page<Order> findOrderByTenant(Tenant tenant, Pageable pageable);

}
