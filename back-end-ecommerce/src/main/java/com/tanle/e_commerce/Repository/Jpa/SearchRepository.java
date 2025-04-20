package com.tanle.e_commerce.Repository.Jpa;

import com.tanle.e_commerce.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchRepository extends JpaRepository<Product,Integer>, JpaSpecificationExecutor<Product> {
}
