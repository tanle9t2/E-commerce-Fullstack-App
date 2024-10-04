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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class TenantController {
    @Autowired
    private TenantService tenantService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenSerice tokenSerice;
    @Autowired
    private UserService userService;
    @GetMapping("/tenant/{tenantId}")
    private ResponseEntity<TenantDTO> getTenant(@PathVariable int tenantId) {
        TenantDTO tenantDTO = tenantService.findById(tenantId);
        return new ResponseEntity<>(tenantDTO, HttpStatus.OK);
    }

    @PostMapping("/tenant/register")
    private ResponseEntity<MessageResponse> registerTenant(@RequestParam TenantRegisterRequest request)  {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        TenantDTO tenantDTO= tenantService.checkTenant(request.getTenantId(),authentication);
        tenantDTO.setName(request.getStoreName());
        tenantDTO.setEmail(request.getEmail());
        tenantDTO.setPhoneNumber(request.getPhoneNumber());
        tenantDTO.setPickupAddressId(request.getPickupAddressId());
        tenantDTO.setReturnAddressId(request.getReturnAddressId());
        tenantDTO.setActive(true);

        MessageResponse response = tenantService.update(tenantDTO);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @PostMapping("/tenant/login")
    private ResponseEntity<MessageResponse> loginTenant(@RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            MessageResponse tokenMessage= tokenSerice.registerToken(request.getUsername());
            TenantDTO tenantDTO = tenantService.checkTenant(request.getUsername(),authentication);
            Map<String,Object> response = new HashMap<>();
            response.put("Tenant",tenantDTO);
            response.put("Token",tokenMessage.getData());

            MessageResponse messageResponse = MessageResponse.builder()
                    .data(response)
                    .status(HttpStatus.OK)
                    .message("login successfully")
                    .build();
            return new ResponseEntity<>(messageResponse,HttpStatus.OK);

        } catch (ResourceNotFoundExeption e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    MessageResponse.builder()
                            .data(null)
                            .message(e.getMessage())
                            .status(HttpStatus.BAD_REQUEST)
                            .build()
            );
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body( MessageResponse.builder()
                    .data(null)
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }
    }
    
}
