package com.tanle.e_commerce.service.serviceimpl;

import com.tanle.e_commerce.Repository.Jpa.CategoryRepository;
import com.tanle.e_commerce.Repository.Jpa.ProductRepository;
import com.tanle.e_commerce.Repository.Jpa.TenantRepository;
import com.tanle.e_commerce.dto.CategoryDTO;
import com.tanle.e_commerce.entities.Category;
import com.tanle.e_commerce.entities.Product;
import com.tanle.e_commerce.entities.Tenant;
import com.tanle.e_commerce.exception.ResourceNotFoundExeption;
import com.tanle.e_commerce.mapper.CategoryMapper;
import com.tanle.e_commerce.payload.MessageResponse;
import com.tanle.e_commerce.payload.PageResponse;
import com.tanle.e_commerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private TenantRepository tenantRepository;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public CategoryDTO findById(Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundExeption("Category not found"));

        return categoryMapper.convertDTO(category);
    }
    @Override
    @Transactional
    public CategoryDTO createCategory(Integer parentId, CategoryDTO category) {
        Tenant tenant = tenantRepository.findById(category.getTenantId())
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found tenant"));
        Category parent = categoryRepository.byIdAndTenant(parentId,tenant.getId())
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found parent category"));

        Category categoryDB = categoryMapper.convertEntity(category);
        categoryDB.setCreateAt(LocalDateTime.now());
        categoryDB  = categoryRepository.createCategory(parent, categoryDB);

        return categoryMapper.convertDTO(categoryDB);
    }
    @Override
    @Transactional
    public MessageResponse deleteCategory(Integer parentId) {
        Category category = categoryRepository.findById(parentId)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found category"));
        List<Category> categories =new ArrayList<>();
        categories.addAll( categoryRepository.getSubCategory(category.getName()
                ,Pageable.unpaged()).getContent());
        categories.add(category);

        //remove foreign key
        categories.forEach(c -> c.removeProduct());

        categoryRepository.updateIndexDeleteCategory(category.getLeft(), category.getRight());
        categoryRepository.deleteAll(categories);

        return MessageResponse.builder()
                .message("Successfully delete category")
                .status(HttpStatus.OK)
                .data(categories.stream()
                        .map(Category::getId)
                        .collect(Collectors.toList()))
                .build();
    }
    @Override
    public PageResponse<CategoryDTO> getSubCategory(String name, Pageable pageable) {
        Page<Category> categories = categoryRepository.getSubCategory(name,pageable);
        if (categories.getNumberOfElements() == 0) {
            return new PageResponse<>(Collections.emptyList(), categories.getNumber() + 1
                    , categories.getNumberOfElements(), categories.getTotalElements(), HttpStatus.OK);
        }
        List<CategoryDTO >categoryDTOS = categories.getContent().stream()
                .map(Category::convertDTO)
                .collect(Collectors.toList());
        return new PageResponse<>(categoryDTOS, categories.getNumber() + 1
                , categories.getNumberOfElements(), categories.getTotalElements(), HttpStatus.OK);
    }

    @Override
    @Transactional
    public Map<String, Object> addProductToCategory(Integer categoryId, List<Integer> productList) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found category"));
        for (Integer productId :productList) {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ResourceNotFoundExeption("Not found product"));
            category.addProduct(product);
        }
        categoryRepository.save(category);
        Map<String,Object> result = new HashMap<>();
        result.put("categoryId",categoryId);
        result.put("currentCount",category.getProducts().size());
        return result;
    }

    @Override
    public Map<String, Object> removeProductFromCategory(Integer categoryId, List<Integer> productList) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found category"));
        for (Integer productId :productList) {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ResourceNotFoundExeption("Not found product"));
            category.removeProduct(product);
        }
        categoryRepository.save(category);
        Map<String,Object> result = new HashMap<>();
        result.put("categoryId",categoryId);
        result.put("currentCount",category.getProducts().size());
        return result;
    }

    @Override
    @Transactional
    public CategoryDTO update(int categoryId, CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found category"));
        category = categoryMapper.update(categoryDTO.getName(),categoryDTO.getDescription(),category);

        categoryRepository.save(category);
        return categoryMapper.convertDTO(category);
    }

    @Override
    public PageResponse<CategoryDTO> findAll(Pageable pageable) {
        Page<Category> categories = categoryRepository.findAll(pageable);
        if (categories.getNumberOfElements() == 0) {
            return new PageResponse<>(Collections.emptyList(), categories.getNumber() + 1
                    , categories.getNumberOfElements(), categories.getTotalElements(), HttpStatus.OK);
        }
        List<CategoryDTO >categoryDTOS = categories.getContent().stream()
                .map(Category::convertDTO)
                .collect(Collectors.toList());
        return new PageResponse<>(categoryDTOS, categories.getNumber() + 1
                , categories.getNumberOfElements(), categories.getTotalElements(), HttpStatus.OK);
    }

}
