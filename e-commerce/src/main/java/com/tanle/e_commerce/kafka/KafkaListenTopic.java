package com.tanle.e_commerce.kafka;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tanle.e_commerce.dto.CategoryDTO;
import com.tanle.e_commerce.dto.ProductDTO;
import com.tanle.e_commerce.dto.ProductDocument;
import com.tanle.e_commerce.dto.SKUDTO;
import com.tanle.e_commerce.mapper.deserialization.CategoryDocumentDeserializer;
import com.tanle.e_commerce.mapper.deserialization.ProductDocumentSerializer;
import com.tanle.e_commerce.mapper.deserialization.SkuDocumentDeserializer;
import com.tanle.e_commerce.service.ProductAsycnService;

import com.tanle.e_commerce.utils.KafkaOperator;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class KafkaListenTopic {

    private final ProductAsycnService productAsycnService;

    @Autowired
    public KafkaListenTopic(ProductAsycnService productAsycnService) {
        this.productAsycnService = productAsycnService;

    }

    @KafkaListener(topics = "${application.kafka.topics.topic2}")
    public void handleSKUTopic(ConsumerRecord<?, ?> consumerRecord) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(SKUDTO.class, new SkuDocumentDeserializer())
                .create();
        JsonObject json = gson.fromJson((String) consumerRecord.value(), JsonObject.class);
        if (json != null) {
            JsonObject payload = json.get("payload").getAsJsonObject();
            if (payload != null) {
                String op = payload.get("op").toString().replace("\"", "");
                Integer productId = null;
                Integer skuId = null;

                switch (op) {
                    case KafkaOperator.CREATE:
                        productId = Integer.parseInt(payload.get("after").getAsJsonObject().get("product_id").toString());
                        skuId = Integer.parseInt(payload.get("after").getAsJsonObject().get("sku_id").toString());
                        productAsycnService.createSku(productId, skuId);
                        break;
                    case KafkaOperator.UPDATE:
                        productId = Integer.parseInt(payload.get("after").getAsJsonObject().get("product_id").toString());
                        SKUDTO skudto = gson.fromJson(payload.get("after"), SKUDTO.class);

                        productAsycnService.updateSKU(productId, skudto);
                        break;
                    case KafkaOperator.DELETE:
                        productId = Integer.parseInt(payload.get("before").getAsJsonObject().get("product_id").toString());
                        skuId = Integer.parseInt(payload.get("before").getAsJsonObject().get("sku_id").toString());
                        productAsycnService.deleteSku(productId, skuId);
                        break;
                }
            }
        }
    }

    @KafkaListener(topics = "${application.kafka.topics.topic3}")
    public void handleCategoryTopic(ConsumerRecord<?, ?> consumerRecord) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(CategoryDTO.class, new CategoryDocumentDeserializer())
                .create();
        JsonObject json = gson.fromJson((String) consumerRecord.value(), JsonObject.class);
        if (json != null) {
            JsonObject payload = json.get("payload").getAsJsonObject();
            if (payload != null) {
                String op = payload.get("op").toString().replace("\"", "");
                Integer productId = null;
                switch (op) {
                    case KafkaOperator.UPDATE:
                        CategoryDTO categoryDTO = gson.fromJson(payload.get("after"), CategoryDTO.class);
                        productId = Integer.parseInt(payload.get("after").getAsJsonObject().get("product_category_id").toString());
                        productAsycnService.updateCategory(productId, categoryDTO);
                        break;
                    case KafkaOperator.DELETE:
                        productId = Integer.parseInt(payload.get("before").getAsJsonObject().get("product_id").toString());
                        productAsycnService.delete(productId);
                        break;
                }
            }
        }
    }

    @KafkaListener(topics = "${application.kafka.topics.topic1}", groupId = "search-asycn")
    public void handleMessage(ConsumerRecord<?, ?> consumerRecord) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(ProductDocument.class, new ProductDocumentSerializer())
                .create();
        JsonObject json = gson.fromJson((String) consumerRecord.value(), JsonObject.class);
        if (json != null) {
            JsonObject payload = json.get("payload").getAsJsonObject();
            if (payload != null) {
                String op = payload.get("op").toString().replace("\"", "");
                Integer id = null;
                switch (op) {
                    case KafkaOperator.CREATE, KafkaOperator.READ:
                        id = Integer.parseInt(payload.get("after").getAsJsonObject().get("product_id").toString());
                        productAsycnService.create(id);
                        break;
                    case KafkaOperator.UPDATE:
                        ProductDocument productDocument = gson.fromJson(payload.get("after"), ProductDocument.class);
                        productAsycnService.update(productDocument);
                        break;
                    case KafkaOperator.DELETE:
                        id = Integer.parseInt(payload.get("before").getAsJsonObject().get("product_id").toString());
                        productAsycnService.delete(id);
                        break;
                }
            }
        }
    }
}
