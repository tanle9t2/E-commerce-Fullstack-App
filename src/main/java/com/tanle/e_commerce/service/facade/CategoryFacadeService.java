package com.tanle.e_commerce.service.facade;

import com.tanle.e_commerce.service.CategoryService;
import com.tanle.e_commerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryFacadeService {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;

}
