package com.tanle.e_commerce.kafka;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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
        JsonObject json = new Gson().fromJson((String) consumerRecord.value(), JsonObject.class);
        if (json != null) {
            JsonObject payload = json.get("payload").getAsJsonObject();
            if (payload != null) {
                String op = payload.get("op").toString().replace("\"", "");
                Integer id = null;
                Integer skuId = null;

                switch (op) {
                    case KafkaOperator.CREATE:
                        id = Integer.parseInt(payload.get("after").getAsJsonObject().get("product_id").toString());
                        skuId = Integer.parseInt(payload.get("after").getAsJsonObject().get("sku_id").toString());
                        productAsycnService.createSku(id, skuId);
                        break;
                    case KafkaOperator.UPDATE:
                        id = Integer.parseInt(payload.get("before").getAsJsonObject().get("product_id").toString());
                        skuId = Integer.parseInt(payload.get("after").getAsJsonObject().get("sku_id").toString());

                        productAsycnService.updateSKU(id, skuId, payload);
                        break;
                    case KafkaOperator.DELETE:
                        id = Integer.parseInt(payload.get("before").getAsJsonObject().get("product_id").toString());
                        skuId = Integer.parseInt(payload.get("before").getAsJsonObject().get("sku_id").toString());
                        productAsycnService.deleteSku(id, skuId);
                        break;
                }
            }
        }
    }

    @KafkaListener(topics = "${application.kafka.topics.topic3}")
    public void handleCategoryTopic(ConsumerRecord<?, ?> consumerRecord) {
        JsonObject json = new Gson().fromJson((String) consumerRecord.value(), JsonObject.class);
        if (json != null) {
            JsonObject payload = json.get("payload").getAsJsonObject();
            if (payload != null) {
                String op = payload.get("op").toString().replace("\"", "");
                Integer id = null;
                switch (op) {
                    case KafkaOperator.UPDATE:
                        id = Integer.parseInt(payload.get("after").getAsJsonObject().get("product_category_id").toString());
                        productAsycnService.updateCategory(id, payload);
                        break;
                    case KafkaOperator.DELETE:
                        id = Integer.parseInt(payload.get("before").getAsJsonObject().get("product_id").toString());
                        productAsycnService.delete(id);
                        break;
                }
            }
        }
    }

    @KafkaListener(topics = "${application.kafka.topics.topic1}", groupId = "search-asycn")
    public void handleMessage(ConsumerRecord<?, ?> consumerRecord) {
        JsonObject json = new Gson().fromJson((String) consumerRecord.value(), JsonObject.class);
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
                        id = Integer.parseInt(payload.get("after").getAsJsonObject().get("product_id").toString());
                        productAsycnService.update(id, payload);
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
