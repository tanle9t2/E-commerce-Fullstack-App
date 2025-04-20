package com.tanle.e_commerce.service.facade;

import com.tanle.e_commerce.dto.ProductDTO;
import com.tanle.e_commerce.entities.Category;
import com.tanle.e_commerce.entities.Product;
import com.tanle.e_commerce.entities.SKU;
import com.tanle.e_commerce.mapper.CategoryMapper;
import com.tanle.e_commerce.mapper.ProductMapper;
import com.tanle.e_commerce.request.ProductCreationRequest;
import com.tanle.e_commerce.service.CategoryService;
import com.tanle.e_commerce.service.ProductService;
import com.tanle.e_commerce.service.SKUService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductFacadeService {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private SKUService skuService;

    @Transactional
    public ProductDTO creatProduct(ProductCreationRequest productCreationRequest) {
        Product product = productCreationRequest.getProduct();
//        product.setOptions(productCreationRequest.getOptions());
//



//
//        Category category =  categoryMapper.convertEntity(categoryService.findById(product.getCategory().getId()));
//        product.setCategory(category);
//        ProductDTO productDTO = productService.save(productCreationRequest.getProduct());
//        product = productMapper.toEntity(productDTO);
//
//        skuService.createSKU(productCreationRequest.getSkus(),product);
//
//        return productDTO;
        return null;
    }
}
