package com.tanle.e_commerce.service.serviceimpl;

import com.tanle.e_commerce.Repository.Jpa.ProductRepository;
import com.tanle.e_commerce.Repository.Jpa.TenantRepository;
import com.tanle.e_commerce.dto.TenantDTO;
import com.tanle.e_commerce.entities.Tenant;
import com.tanle.e_commerce.exception.ResourceNotFoundExeption;
import com.tanle.e_commerce.mapper.TenantMapper;
import com.tanle.e_commerce.payload.MessageResponse;
import com.tanle.e_commerce.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;


@Service
public class TenantServiceImpl implements TenantService {
    @Autowired
    private TenantRepository tenantRepository;
    @Autowired
    private TenantMapper tenantMapper;
    @Autowired
    private ProductRepository productRepository;

    @Override
    @Transactional
    public MessageResponse update(TenantDTO tenantDTO) {
        Tenant tenant = tenantRepository.findById(tenantDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found tenant"));
        tenant = tenantMapper.updateExisting(tenant,tenantDTO);
        tenant.setCreateAt(LocalDateTime.now());

        tenantRepository.save(tenant);
        return MessageResponse.builder()
                .status(HttpStatus.OK)
                .message("Update tenant successfully")
                .data(new HashMap<>().put("tenanId",tenant.getId()))
                .build();
    }

    @Override
    @Transactional
    public TenantDTO save(Tenant tenant) {
        return null;
    }
    @Override
    public TenantDTO findById(Integer id) {
        Tenant tenant = tenantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found tenant"));
        Long totalProduct = productRepository.sumProductByTenet(id);
        TenantDTO tenantDTO = tenant.converDTO();
        tenantDTO.setTotalProduct(totalProduct);
        return tenantDTO;
    }

    @Override
    public TenantDTO checkTenant(String username, Authentication authentication) {
        Tenant tenant = tenantRepository.findByUser(username)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found tenant"));
        if(tenant.getUser().getUsername() != authentication.getName())
            throw new BadCredentialsException("User don't have permission");
        if(!tenant.isActive())
            throw new RuntimeException(username +" isn't seller");
        return tenant.converDTO();
    }

    @Override
    public TenantDTO checkTenant(int tenantId, Authentication authentication) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found tenant"));
        if(tenant.getUser().getUsername() != authentication.getName())
            throw new BadCredentialsException("User don't have permission");
        return tenant.converDTO();
    }


}
