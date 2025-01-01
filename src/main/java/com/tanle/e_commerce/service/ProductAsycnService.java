package com.tanle.e_commerce.service;

import com.google.gson.JsonObject;

public interface ProductAsycnService {
    void create(int entityId);

    void update(int entityId, JsonObject payload);

    void delete(int entityId);

    void deleteSku(int entityId, int skuId);

    void createSku(int entityId, int skuId);

    void updateCategory(int entityId, JsonObject payload);

    void updateSKU(int entityId, int skuId, JsonObject payload);

}
