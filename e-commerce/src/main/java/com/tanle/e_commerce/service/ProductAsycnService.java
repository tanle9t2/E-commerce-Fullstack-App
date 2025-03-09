package com.tanle.e_commerce.service;

import com.google.gson.JsonObject;
import com.tanle.e_commerce.dto.CategoryDTO;
import com.tanle.e_commerce.dto.ProductDocument;
import com.tanle.e_commerce.dto.SKUDTO;

public interface ProductAsycnService {
    void create(int entityId);

    void update(ProductDocument productDocument);

    void delete(int entityId);

    void deleteSku(int entityId, int skuId);

    void createSku(int entityId, int skuId);

    void updateCategory(int entityId, CategoryDTO categoryDTO);

    void updateSKU(Integer productId, SKUDTO skudto);

}
