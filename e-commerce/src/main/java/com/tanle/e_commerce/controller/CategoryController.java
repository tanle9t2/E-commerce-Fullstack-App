package com.tanle.e_commerce.controller;

import com.tanle.e_commerce.dto.CategoryDTO;
import com.tanle.e_commerce.respone.CategoryFilterResponse;
import com.tanle.e_commerce.respone.MessageResponse;
import com.tanle.e_commerce.respone.PageResponse;
import com.tanle.e_commerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.tanle.e_commerce.utils.AppConstant.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    public ResponseEntity<?> getAllCategory(
            @RequestParam(value = "page", required = false, defaultValue = PAGE_DEFAULT) int page,
            @RequestParam(value = "subcategory", required = false, defaultValue = "Root") String parent
    ) {

        List<CategoryDTO> result = categoryService.getSubCategory(parent);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/category/tenant")
    public ResponseEntity<List<CategoryDTO>> getCategoryByTenant(@RequestParam(value = "tenantId") String tenantId) {
        List<CategoryDTO> categoryDTOS = categoryService.findByTenantId(Integer.parseInt(tenantId));
        return ResponseEntity.ok(categoryDTOS);
    }

    @GetMapping("/category/level")
    public ResponseEntity<List<CategoryFilterResponse>> getCategoryFollowLevel(@RequestParam(value = "tenantId") String tenantId,
                                                                               @RequestParam(value = "level") String level) {
        List<CategoryFilterResponse> categoryDTOS = categoryService.getCategoryFollowLevel(Integer.parseInt(tenantId)
                , Integer.parseInt(level));
        return ResponseEntity.ok(categoryDTOS);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<CategoryDTO> getCategory(@PathVariable int categoryId) {
        CategoryDTO category = categoryService.findById(categoryId);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @PostMapping("/category")
    public ResponseEntity<CategoryDTO> createCategory(
            @RequestBody CategoryDTO category,
            @RequestParam(value = "parent", required = false, defaultValue = ROOT_ELEMENT) String parent) {
        CategoryDTO categories = categoryService.createCategory(Integer.parseInt(parent), category);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @DeleteMapping("/category/{categoryId}")
    public ResponseEntity<MessageResponse> deleteCategory(@PathVariable int categoryId) {
        MessageResponse messageResponse = categoryService.deleteCategory(categoryId);

        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    @PutMapping("/category/listproduct")
    public ResponseEntity<Map<String, Object>> addProductToCatogory(
            @RequestParam(name = "categoryId") String categoryId,
            @RequestBody List<Integer> productList
    ) {
        Map<String, Object> response = categoryService.addProductToCategory(Integer.parseInt(categoryId), productList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/category/listproduct")
    public ResponseEntity<Map<String, Object>> removeProductFromCategory(
            @RequestParam(name = "categoryId") String categoryId,
            @RequestBody List<Integer> productList
    ) {
        Map<String, Object> response = categoryService.removeProductFromCategory(Integer.parseInt(categoryId), productList);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PutMapping("/category/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable int categoryId
            , @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO respone = categoryService.update(categoryId, categoryDTO);
        return new ResponseEntity<>(respone, HttpStatus.OK);
    }
}
