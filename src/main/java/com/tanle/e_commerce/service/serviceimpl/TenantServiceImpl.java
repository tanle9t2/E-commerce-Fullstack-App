package com.tanle.e_commerce.service.serviceimpl;

import com.tanle.e_commerce.Repository.Jpa.ProductRepository;
import com.tanle.e_commerce.Repository.Jpa.TenantRepository;
import com.tanle.e_commerce.dto.TenantDTO;
import com.tanle.e_commerce.entities.Tenant;
import com.tanle.e_commerce.exception.ResourceNotFoundExeption;
import com.tanle.e_commerce.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class TenantServiceImpl implements TenantService {
    @Autowired
    private TenantRepository tenantRepository;
    @Autowired
    private ProductRepository productRepository;

    @Override
    @Transactional
    public TenantDTO update(Tenant tenant) {
        return null;
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

}
