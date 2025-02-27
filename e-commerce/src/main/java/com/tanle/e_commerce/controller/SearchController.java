package com.tanle.e_commerce.controller;

import com.tanle.e_commerce.dto.ProductDTO;
import com.tanle.e_commerce.entities.Product;
import com.tanle.e_commerce.respone.FilterSearchResponse;
import com.tanle.e_commerce.respone.PageResponse;
import com.tanle.e_commerce.respone.PageSearchResponse;
import com.tanle.e_commerce.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tanle.e_commerce.utils.AppConstant.*;

@RestController
@RequestMapping("/api/v1")
public class SearchController {
    @Autowired
    private SearchService service;

    @GetMapping("/search1")
    public PageResponse<Product> searchKeyword(
            @RequestParam(name = "keyword") String keyword,
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "minPrice", required = false) String minPrice,
            @RequestParam(name = "maxPrice", required = false) String maxPrice,
            @RequestParam(name = "sortBy", required = false, defaultValue = SORT_BY_FIELD) String sortBy,
            @RequestParam(name = "order", required = false, defaultValue = DIRECTION_SORT_DEFAULT) String order,
            @RequestParam(name = "page", required = false, defaultValue = PAGE_DEFAULT) Integer page
    ) {
        Map<String, String> mp = new HashMap<>();
        mp.put("category", category);
        mp.put("keyword", keyword);
        mp.put("minPrice", minPrice);
        mp.put("maxPrice", maxPrice);
        mp.put("sortBy", sortBy);
        mp.put("order", order);
        return service.findBySpecs(mp, page, Integer.parseInt(PAGE_SIZE));
    }

    @GetMapping("/search-hint")
    public ResponseEntity<Map<Integer, String>> getHint(@RequestParam(name = "keyword") String keyword) {
        Map<Integer, String> response = service.searchHint(keyword);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/filter-search")
    public ResponseEntity<List<FilterSearchResponse>> getFilterSearch(
            @RequestParam Map<String, String> params) {
        List<FilterSearchResponse> response = service.getFilterSearch(params);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<PageSearchResponse> search(
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "minPrice", required = false) String minPrice,
            @RequestParam(name = "maxPrice", required = false) String maxPrice,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "order", required = false) String order,
            @RequestParam(name = "location", required = false) String location,
            @RequestParam(name = "page", required = false, defaultValue = PAGE_DEFAULT) String page,
            @RequestParam(name = "size", required = false, defaultValue = PAGE_SIZE) String size
    ) {
        Map<String, String> mp = new HashMap<>();
        mp.put("category", category);
        mp.put("keyword", keyword);
        mp.put("minPrice", minPrice);
        mp.put("maxPrice", maxPrice);
        mp.put("sortBy", sortBy);
        mp.put("order", order);
        mp.put("location", location);
        PageSearchResponse pageSearchResponse = service.searchProduct(mp, Integer.parseInt(page), Integer.parseInt(size));
        return ResponseEntity.ok(pageSearchResponse);
    }

    @GetMapping("/search3")
    public ProductDTO test(@RequestParam("id") String id) {
        return service.test(id);
    }
}
