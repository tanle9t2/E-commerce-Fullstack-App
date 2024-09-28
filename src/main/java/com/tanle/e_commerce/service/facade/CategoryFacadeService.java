package com.tanle.e_commerce.service.facade;

import com.tanle.e_commerce.dto.CategoryDTO;
import com.tanle.e_commerce.entities.Category;
import com.tanle.e_commerce.entities.Product;
import com.tanle.e_commerce.payload.MessageResponse;
import com.tanle.e_commerce.payload.PageResponse;
import com.tanle.e_commerce.service.CategoryService;
import com.tanle.e_commerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryFacadeService {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;

}
