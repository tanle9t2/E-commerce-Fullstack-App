package com.tanle.e_commerce.service;

import com.tanle.e_commerce.dto.TenantDTO;
import com.tanle.e_commerce.entities.Tenant;
import com.tanle.e_commerce.payload.ApiResponse;
import com.tanle.e_commerce.payload.MessageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface TenantService {
    MessageResponse update(TenantDTO tenant);
    TenantDTO save(Tenant tenant);
    TenantDTO findById(Integer id);

    TenantDTO checkTenant(String username, Authentication authentication);
    TenantDTO checkTenant(int tenantId, Authentication authentication);
}
