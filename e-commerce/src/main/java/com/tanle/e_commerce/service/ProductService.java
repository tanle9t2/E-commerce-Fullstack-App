package com.tanle.e_commerce.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tanle.e_commerce.dto.ProductDTO;
import com.tanle.e_commerce.entities.Product;
import com.tanle.e_commerce.respone.MessageResponse;
import com.tanle.e_commerce.respone.PageResponse;
import com.tanle.e_commerce.request.ProductCreationRequest;
import com.tanle.e_commerce.service.authorization.OwnerService;
import org.springframework.data.domain.Pageable;

import javax.json.JsonPatch;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface ProductService extends OwnerService<Product, Integer> {

    PageResponse<ProductDTO> findAll(int page, int size, String direction, String... field);

    PageResponse<ProductDTO> findByTenant(int tenantId, int page, int size, String direction, String... field);

    PageResponse<ProductDTO> findByName(int page, int size, String nameProduct);

    ProductDTO findById(Integer id);

    PageResponse<ProductDTO> findByCategory(String categoryId, Pageable pageable);

    List<ProductDTO> findByOption(Integer optionId);

    ProductDTO update(Product product);

    void delete(Integer id);

    ProductDTO save(ProductCreationRequest productCreationRequest);

    ProductDTO save(Product product);

    ProductDTO update(Integer id, JsonPatch jsonPatch, String skuNo) throws JsonProcessingException;

    PageResponse<ProductDTO> findByTenant(int page, int size, Integer tenantId);

    ProductDTO addOption(Integer productId, ProductCreationRequest request);

    MessageResponse deleteOption(List<Integer> skusId, List<Integer> optionId
            , List<LinkedHashMap<Integer, List<Integer>>> optionValuesId, int productId);

    ProductDTO updatePrice(Integer productId, Map<String, Integer> data);

    ProductDTO updateStock(Integer productId, Map<String, Integer> data);
}
