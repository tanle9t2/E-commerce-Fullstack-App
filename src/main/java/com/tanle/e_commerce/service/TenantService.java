package com.tanle.e_commerce.service;

import com.tanle.e_commerce.dto.TenantDTO;
import com.tanle.e_commerce.entities.Tenant;
import com.tanle.e_commerce.request.TenantRegisterRequest;
import com.tanle.e_commerce.respone.MessageResponse;
import com.tanle.e_commerce.service.authorization.OwnerService;
import org.springframework.security.core.Authentication;

public interface TenantService extends OwnerService<Tenant,Integer> {
    MessageResponse update(TenantDTO tenant);
    TenantDTO save(Tenant tenant);
    TenantDTO findById(Integer id);
    MessageResponse registerInformation(TenantRegisterRequest tenantRegisterRequest);

    TenantDTO authenticate(String username, Authentication authentication);
    TenantDTO authenticate(int tenantId, Authentication authentication);

    MessageResponse inActiveTenant(Integer tenantId);
}
