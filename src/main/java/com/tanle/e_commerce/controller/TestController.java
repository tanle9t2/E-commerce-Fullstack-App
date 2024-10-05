package com.tanle.e_commerce.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tanle.e_commerce.dto.ProductDTO;
import com.tanle.e_commerce.entities.Product;
import com.tanle.e_commerce.respone.ApiResponse;
import com.tanle.e_commerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class TestController {
    @Autowired
    private ProductService productService;

    @PostMapping("/product")
    public ApiResponse<ProductDTO> getProduct (@RequestBody Product product) throws JsonProcessingException {
//        ApiResponse<ProductDTO> products = productService.save(product);
        return null;
    }

}
