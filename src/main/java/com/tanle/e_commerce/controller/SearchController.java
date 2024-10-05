package com.tanle.e_commerce.controller;

import com.tanle.e_commerce.dto.ProductDTO;
import com.tanle.e_commerce.entities.Product;
import com.tanle.e_commerce.respone.PageResponse;
import com.tanle.e_commerce.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static com.tanle.e_commerce.utils.AppConstant.*;

@RestController
@RequestMapping("/api/v1")
public class SearchController {
    @Autowired
    private SearchService service;

    @GetMapping("/search")
    public PageResponse<Product> searchKeyword(
            @RequestParam(name = "keyword") String keyword,
            @RequestParam(name = "category",required = false) String category,
            @RequestParam(name ="minPrice", required = false) String minPrice ,
            @RequestParam(name = "maxPrice", required = false) String maxPrice,
            @RequestParam(name = "sortBy",required = false, defaultValue = SORT_BY_FIELD) String sortBy,
            @RequestParam(name = "order",required = false, defaultValue = DIRECTION_SORT_DEFAULT) String order,
            @RequestParam(name = "page", required = false, defaultValue = PAGE_DEFAULT) Integer page
    ) {
        Map<String,String> mp = new HashMap<>();
        mp.put("category",category);
        mp.put("keyword",keyword);
        mp.put("minPrice",minPrice);
        mp.put("maxPrice",maxPrice);
        mp.put("sortBy",sortBy);
        mp.put("order",order);
        return service.findBySpecs(mp,page,Integer.parseInt(PAGE_SIZE));
    }
    @GetMapping("/search2")
    public PageResponse<ProductDTO> search(
            @RequestParam(name = "keyword") String keyword,
            @RequestParam(name = "category",required = false) String category,
            @RequestParam(name ="minPrice", required = false) String minPrice ,
            @RequestParam(name = "maxPrice", required = false) String maxPrice,
            @RequestParam(name = "sortBy",required = false) String sortBy,
            @RequestParam(name = "order",required = false) String order,
            @RequestParam(name = "page", required = false, defaultValue = PAGE_DEFAULT) String page
    ) {
        Map<String,String> mp = new HashMap<>();
        mp.put("category",category);
        mp.put("keyword",keyword);
        mp.put("minPrice",minPrice);
        mp.put("maxPrice",maxPrice);
        mp.put("sortBy",sortBy);
        mp.put("order",order);
        return service.searchProduct(mp,Integer.parseInt(page),10);
    }
    @GetMapping("/search3")
    public ProductDTO test (@RequestParam("id") String id) {
        return service.test(id);
    }
}
