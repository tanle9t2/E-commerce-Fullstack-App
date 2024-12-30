package com.tanle.e_commerce.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tanle.e_commerce.dto.ProductDTO;
import com.tanle.e_commerce.dto.SKUDTO;
import com.tanle.e_commerce.entities.Option;
import com.tanle.e_commerce.respone.ApiResponse;
import com.tanle.e_commerce.respone.MessageResponse;
import com.tanle.e_commerce.respone.PageResponse;
import com.tanle.e_commerce.request.ProductCreationRequest;
import com.tanle.e_commerce.service.OptionService;
import com.tanle.e_commerce.service.ProductService;
import com.tanle.e_commerce.service.SKUService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.json.JsonPatch;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.tanle.e_commerce.utils.AppConstant.*;

@RestController
@RequestMapping("/api/v1/")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private SKUService skuService;
    @Autowired
    private OptionService optionService;

    @RequestMapping(value = "/product_list",method = RequestMethod.GET)
    public ResponseEntity<?> getProducts (
            @RequestParam(name = "page", required = false, defaultValue = PAGE_DEFAULT) Integer page,
            @RequestParam(name = "sort", required = false, defaultValue = "id") String field,
            @RequestParam(name = "order", required = false, defaultValue = DIRECTION_SORT_DEFAULT)String direction
    ) {
        PageResponse<ProductDTO> products = productService.findAll(page,Integer.parseInt(PAGE_SIZE),direction,field);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
    @GetMapping("/product/{productId}")
    public ResponseEntity<ProductDTO> getProduct (
            @PathVariable int productId
            ,@RequestParam(value = "optionValue",required = false)String optionValue) {
        ProductDTO product = productService.findById(productId);
        return new ResponseEntity<>(product,HttpStatus.OK);
    }

    @GetMapping("/products")
    public ResponseEntity<?> getProductByCategory(
            @RequestParam("categoryname") String categoryName,
            @RequestParam(value = "page", required = false,defaultValue = PAGE_DEFAULT) Integer page) {
        Pageable pageable = PageRequest.of(page,Integer.parseInt(PAGE_SIZE));
        PageResponse<ProductDTO> productPageResponse = productService.findByCategory(categoryName,pageable);
        return new ResponseEntity<>(productPageResponse,HttpStatus.OK);
    }



    @PatchMapping(path = "/product/{id}", consumes = "application/json-patch+json")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Integer id
            , @RequestBody JsonPatch jsonPath
            ,@RequestParam(value = "sku",required = false) String skuNo) throws JsonProcessingException {
        ProductDTO productDTO = productService.update(id,jsonPath,skuNo);
        return new ResponseEntity<>(productDTO,HttpStatus.OK);
    } ;

    @PostMapping("/product/options")
    public ResponseEntity<ProductDTO> addOptions(@RequestParam(name = "productId") String productId
            , @RequestBody ProductCreationRequest request) {
        ProductDTO productDTO = productService.addOption(Integer.parseInt(productId),request);
        return new ResponseEntity<>(productDTO,HttpStatus.OK);
    }
    @PutMapping("/product/options")
    public ResponseEntity<MessageResponse> updateOptions(@RequestBody Map<String, List<Option>> data) {
        MessageResponse messageResponse = optionService.updateOption(data);
        return new ResponseEntity<>(messageResponse,HttpStatus.OK);
    }
    @PutMapping("/product/sku")
    public ResponseEntity<List<SKUDTO>> updateSKU(@RequestParam(name = "productId") String productId
            , @RequestBody List<SKUDTO> skudtos) {
        List<SKUDTO> respone = skuService.updateSKU(Integer.parseInt(productId),skudtos);
        return new ResponseEntity<>(respone,HttpStatus.OK);
    }
    @DeleteMapping("/product/options")
    public ResponseEntity<MessageResponse> deleteOption(
            @RequestBody Map<String,Object> mp
            , @RequestParam("productId") String productId) {

        List<Integer> skusId = (List<Integer>) mp.getOrDefault("skusId",null);
        List<Integer> optionsId = (List<Integer>) mp.getOrDefault("optionsId",null);
        List<LinkedHashMap<Integer,List<Integer>>> optionValuesId =
                (List<LinkedHashMap<Integer, List<Integer>>>) mp.getOrDefault("optionValuesId",null);

        MessageResponse messageResponse = productService.deleteOption(skusId,optionsId,
                optionValuesId,Integer.parseInt(productId));
        return new ResponseEntity<>(messageResponse,HttpStatus.OK);
    }

    @PostMapping("/product")
    public ApiResponse<ProductDTO> createProduct(@RequestBody ProductCreationRequest product) {
        ProductDTO productDTO=  productService.save(product);


        return ApiResponse.<ProductDTO>builder()
                .data(productDTO)
                .status(HttpStatus.OK)
                .message("Successfully create Product")
                .build();
    }
    //update

    @DeleteMapping("/product/{productId}")
    public MessageResponse deleteProduct(@PathVariable int productId) {
        productService.delete(productId);
        return MessageResponse.builder()
                .message("Successfully delete product")
                .status(HttpStatus.OK)
                .build();
    }
    @PutMapping("/product/price")
    public ResponseEntity<ProductDTO> updatePrice(@RequestParam("productId") String productId
            , @RequestBody Map<String,Integer> data) {
        ProductDTO productDTO = productService.updatePrice(Integer.parseInt(productId),data);
        return new ResponseEntity<>(productDTO,HttpStatus.OK);
    }
    @PutMapping("/product/stock")
    public ResponseEntity<ProductDTO> updateStock(@RequestParam("productId") String productId
            , @RequestBody Map<String,Integer> data) {
        ProductDTO productDTO = productService.updateStock(Integer.parseInt(productId),data);
        return new ResponseEntity<>(productDTO,HttpStatus.OK);
    }


}
