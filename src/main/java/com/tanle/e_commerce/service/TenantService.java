package com.tanle.e_commerce.service;

import com.tanle.e_commerce.dto.TenantDTO;
import com.tanle.e_commerce.entities.Tenant;
import com.tanle.e_commerce.payload.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface TenantService {
    TenantDTO update(Tenant tenant);
    TenantDTO save(Tenant tenant);
    TenantDTO findById(Integer id);
}
