package com.tanle.e_commerce.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tanle.e_commerce.dto.ProductDTO;
import com.tanle.e_commerce.entities.Product;
import com.tanle.e_commerce.payload.ApiResponse;
import com.tanle.e_commerce.payload.PageResponse;
import com.tanle.e_commerce.service.ProductService;
import com.tanle.e_commerce.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tanle.e_commerce.utils.AppConstant.PAGE_DEFAULT;
import static com.tanle.e_commerce.utils.AppConstant.PAGE_SIZE;

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
