package com.tanle.e_commerce.Repository.Jpa;

import com.tanle.e_commerce.entities.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TenantRepository extends JpaRepository<Tenant,Integer> {
    @Query(value = "FROM Tenant  where myUser.username = ?1")
    Optional<Tenant> findByUser(String username);
}
