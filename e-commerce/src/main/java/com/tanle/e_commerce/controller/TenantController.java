package com.tanle.e_commerce.controller;


import com.tanle.e_commerce.dto.CategoryDTO;
import com.tanle.e_commerce.dto.ProductDTO;
import com.tanle.e_commerce.dto.TenantDTO;

import com.tanle.e_commerce.respone.MessageResponse;
import com.tanle.e_commerce.request.TenantRegisterRequest;

import com.tanle.e_commerce.respone.PageResponse;
import com.tanle.e_commerce.service.CategoryService;
import com.tanle.e_commerce.service.ProductService;
import com.tanle.e_commerce.service.TenantService;
import com.tanle.e_commerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.tanle.e_commerce.utils.AppConstant.*;

@RestController
@RequestMapping("/api/v1/tenant/")
@CrossOrigin(origins = "http://localhost:5173") // React app URL
public class TenantController extends BaseUserController {
    @Autowired
    private TenantService tenantService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;

    @GetMapping("/{tenantId}")
    private ResponseEntity<Map<String, Object>> getTenant(@PathVariable int tenantId) {
        TenantDTO tenantDTO = tenantService.findById(tenantId);
        PageResponse<ProductDTO> products = productService.findByTenant(Integer.parseInt(PAGE_DEFAULT),
                Integer.parseInt(PAGE_SIZE), tenantId);
        List<CategoryDTO> categoryDTOS = categoryService.findByTenantId(tenantId);

        return ResponseEntity.ok(Map.of("tenantInfor", tenantDTO
                , "products", products
                , "categories", categoryDTOS));
    }

    @GetMapping("/tenant-infor/{tenantId}")
    private ResponseEntity<TenantDTO> getTenantInfor(@PathVariable int tenantId) {
        TenantDTO tenantDTO = tenantService.findById(tenantId);
        return ResponseEntity.ok(tenantDTO);
    }

    @PostMapping("/register")
    private ResponseEntity<MessageResponse> registerTenant(@RequestBody TenantRegisterRequest request) {
        MessageResponse tenant = tenantService.registerInformation(request);
        TenantDTO tenantDTO = (TenantDTO) tenant.getData();
        userService.grantRole(tenantDTO.getUserId(), "SELLER");
        return ResponseEntity.status(HttpStatus.OK).body(tenant);
    }

    @PutMapping("/{tenantId}")
    private ResponseEntity<MessageResponse> inActiveTenant(@PathVariable Integer tenantId) {
        MessageResponse tenant = tenantService.inActiveTenant(tenantId);
        return ResponseEntity.status(HttpStatus.OK).body(tenant);
    }


}
