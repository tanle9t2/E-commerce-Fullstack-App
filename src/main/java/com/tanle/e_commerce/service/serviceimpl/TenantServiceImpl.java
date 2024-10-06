package com.tanle.e_commerce.service.serviceimpl;

import com.tanle.e_commerce.Repository.Jpa.AddressRepository;
import com.tanle.e_commerce.Repository.Jpa.ProductRepository;
import com.tanle.e_commerce.Repository.Jpa.RoleRepository;
import com.tanle.e_commerce.Repository.Jpa.TenantRepository;
import com.tanle.e_commerce.dto.TenantDTO;
import com.tanle.e_commerce.entities.Address;
import com.tanle.e_commerce.entities.Tenant;
import com.tanle.e_commerce.exception.ResourceNotFoundExeption;
import com.tanle.e_commerce.mapper.TenantMapper;
import com.tanle.e_commerce.request.TenantRegisterRequest;
import com.tanle.e_commerce.respone.MessageResponse;
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
    @Autowired
    private AddressRepository addressRepository;

    @Override
    @Transactional
    public MessageResponse update(TenantDTO tenantDTO) {
        Tenant tenant = tenantRepository.findById(tenantDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found tenant"));
        tenant = tenantMapper.updateExisting(tenant,tenantDTO);
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
    @Transactional
    public MessageResponse registerInformation(TenantRegisterRequest tenantRegisterRequest) {
        Tenant tenant = tenantRepository.findById(tenantRegisterRequest.getTenantId())
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found tenant"));
        if(tenantRegisterRequest.getReturnAddressId() != null) {
            Address returnAddress = addressRepository.findById(tenantRegisterRequest.getReturnAddressId())
                    .orElseThrow(() -> new ResourceNotFoundExeption("Not found return address"));
            tenant.setReturnAddress(returnAddress);
        }
        if(tenantRegisterRequest.getPickupAddressId() != null) {
            Address returnAddress = addressRepository.findById(tenantRegisterRequest.getPickupAddressId())
                    .orElseThrow(() -> new ResourceNotFoundExeption("Not found return address"));
            tenant.setPickupAddress(returnAddress);
        }
        tenant.setName(tenantRegisterRequest.getStoreName());
        tenant.setEmail(tenantRegisterRequest.getEmail());
        tenant.setCreateAt(LocalDateTime.now());
        tenant.setPhoneNumber(tenantRegisterRequest.getPhoneNumber());
        tenant.setDomain(tenantRegisterRequest.getDescription());
        tenant.setActive(true);

        tenantRepository.save(tenant);
        return MessageResponse.builder()
                .message("Register information successfully")
                .status(HttpStatus.OK)
                .data(tenantMapper.convertDTO(tenant))
                .build();
    }

    @Override
    public TenantDTO authenticate(String username, Authentication authentication) {
        Tenant tenant = tenantRepository.findByUser(username)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found tenant"));
        if(tenant.getUser().getUsername() != authentication.getName())
            throw new BadCredentialsException("User don't have permission");
        if(!tenant.isActive())
            throw new RuntimeException(username +" isn't seller");
        return tenant.converDTO();
    }

    @Override
    public TenantDTO authenticate(int tenantId, Authentication authentication) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found tenant"));
        if(tenant.getUser().getUsername() != authentication.getName())
            throw new BadCredentialsException("User don't have permission");
        return tenant.converDTO();
    }

    @Override
    public boolean userOwnEntity(Integer integer, String username) {
        Tenant tenant = tenantRepository.findById(integer)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found tenant"));

        return tenant.getUser().getUsername().equals(username);
    }
}
