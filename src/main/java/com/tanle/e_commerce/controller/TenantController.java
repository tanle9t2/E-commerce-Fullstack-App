package com.tanle.e_commerce.controller;

import com.tanle.e_commerce.dto.TenantDTO;
import com.tanle.e_commerce.entities.Tenant;
import com.tanle.e_commerce.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class TenantController {
    @Autowired
    private TenantService tenantService;
    @GetMapping("/tenant/{tenantId}")
    private ResponseEntity<TenantDTO> getTenant(@PathVariable int tenantId) {
        TenantDTO tenantDTO = tenantService.findById(tenantId);
        return new ResponseEntity<>(tenantDTO, HttpStatus.OK);
    }
    
}
