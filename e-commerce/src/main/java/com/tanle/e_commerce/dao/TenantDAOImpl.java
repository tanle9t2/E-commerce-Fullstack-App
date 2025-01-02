package com.tanle.e_commerce.dao;

import com.tanle.e_commerce.entities.Tenant;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TenantDAOImpl implements TenantDAO{
    @Autowired
    private EntityManager entityManager;
    @Override
    public Tenant update(Tenant tenant) {
       Tenant result = entityManager.merge(tenant);
       return result;
    }
}
