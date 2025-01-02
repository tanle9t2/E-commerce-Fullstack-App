package com.tanle.e_commerce.Repository.Jpa;

import com.tanle.e_commerce.entities.OrderCancellation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderCancellationRepository extends JpaRepository<OrderCancellation,Integer> {
}
