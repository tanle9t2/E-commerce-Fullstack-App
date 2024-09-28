package com.tanle.e_commerce.Repository.Jpa;

import com.tanle.e_commerce.entities.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantRepository extends JpaRepository<Tenant,Integer> {
}
