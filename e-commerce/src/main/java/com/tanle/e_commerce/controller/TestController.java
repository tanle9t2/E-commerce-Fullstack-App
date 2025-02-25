package com.tanle.e_commerce.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tanle.e_commerce.Repository.Jpa.ProductRepository;
import com.tanle.e_commerce.dto.ProductDTO;
import com.tanle.e_commerce.entities.Product;
import com.tanle.e_commerce.mapper.ProductMapper;
import com.tanle.e_commerce.respone.ApiResponse;
import com.tanle.e_commerce.service.ProductService;
import com.tanle.e_commerce.service.serviceimpl.ProductAsynServiceImpl;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TestController {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ElasticsearchOperations elasticsearchOperations;
    @Autowired
    private ProductAsynServiceImpl productAsynService;
    @Autowired
    private ProductMapper productMapper;

    @PostMapping("/product")
    public ApiResponse<ProductDTO> getProduct(@RequestBody Product product) throws JsonProcessingException {
//        ApiResponse<ProductDTO> products = productService.save(product);
        return null;
    }
    @GetMapping("/asyn")
    public void asynData() {
        productAsynService.createAll();
    }
    @GetMapping("/test")
    public ResponseEntity test() {
        List<Product> products = productRepository.findAll();
        for (Product p : products) {
            ProductDTO productDTO = productMapper.asInput(p);
            elasticsearchOperations.save(productDTO);
        }
        return ResponseEntity.status(200).body("ok");
    }

}
