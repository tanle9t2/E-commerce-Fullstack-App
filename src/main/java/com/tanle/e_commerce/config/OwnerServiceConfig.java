package com.tanle.e_commerce.config;

import com.tanle.e_commerce.entities.Product;
import com.tanle.e_commerce.service.OrderService;
import com.tanle.e_commerce.service.ProductService;
import com.tanle.e_commerce.service.TenantService;
import com.tanle.e_commerce.service.UserService;
import com.tanle.e_commerce.service.authorization.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.Map;

@Configuration
public class OwnerServiceConfig {
    private final UserService userService;
    private final ProductService productService;
    private final TenantService tenantService;
    private final OrderService orderService;

    @Autowired
    public OwnerServiceConfig(UserService userService, ProductService productService,
                              TenantService tenantService, OrderService orderService) {
        this.userService = userService;
        this.productService = productService;
        this.tenantService = tenantService;
        this.orderService = orderService;
    }

    @Bean("ownerService")
    public Map<String, OwnerService<?, Integer>> ownerServices() {
       Map mp = Map.of(
               "order", orderService,
                "product", productService,
                "user",userService,
                "tenant",tenantService
        );
       return mp;
    }
}
