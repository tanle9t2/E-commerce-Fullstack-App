package com.tanle.e_commerce.controller;

import com.tanle.e_commerce.dto.TenantDTO;
import com.tanle.e_commerce.dto.UserDTO;
import com.tanle.e_commerce.entities.Tenant;
import com.tanle.e_commerce.exception.ResourceNotFoundExeption;
import com.tanle.e_commerce.payload.MessageResponse;
import com.tanle.e_commerce.request.LoginRequest;
import com.tanle.e_commerce.request.TenantRegisterRequest;
import com.tanle.e_commerce.service.TenantService;
import com.tanle.e_commerce.service.TokenSerice;
import com.tanle.e_commerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/tenant/")
public class TenantController extends BaseUserController{
    @Autowired
    private TenantService tenantService;
    @GetMapping("/{tenantId}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SELLER')")
    private ResponseEntity<TenantDTO> getTenant(@PathVariable int tenantId) {
        TenantDTO tenantDTO = tenantService.findById(tenantId);
        return new ResponseEntity<>(tenantDTO, HttpStatus.OK);
    }
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @PostMapping("/register")
    private ResponseEntity<MessageResponse> registerTenant(@RequestParam TenantRegisterRequest request)  {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        TenantDTO tenantDTO= null;
        tenantDTO.setName(request.getStoreName());
        tenantDTO.setEmail(request.getEmail());
        tenantDTO.setPhoneNumber(request.getPhoneNumber());
        tenantDTO.setPickupAddressId(request.getPickupAddressId());
        tenantDTO.setReturnAddressId(request.getReturnAddressId());
        tenantDTO.setActive(true);

        MessageResponse response = tenantService.update(tenantDTO);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
//    @Override
//    @PostMapping("/tenant/login")
//    public ResponseEntity<MessageResponse> login(LoginRequest request) {
//        return super.login(request);
//    }
}
