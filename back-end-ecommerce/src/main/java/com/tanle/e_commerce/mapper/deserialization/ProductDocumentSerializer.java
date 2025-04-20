package com.tanle.e_commerce.mapper.deserialization;

import com.google.gson.*;
import com.tanle.e_commerce.dto.CategoryDTO;
import com.tanle.e_commerce.dto.ProductDocument;

import java.lang.reflect.Type;

import static com.tanle.e_commerce.utils.convert.FieldElasticsearchConvert.*;

public class ProductDocumentSerializer implements JsonDeserializer<ProductDocument> {
    @Override
    public ProductDocument deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject obj = jsonElement.getAsJsonObject();
        ProductDocument productDocument = new ProductDocument();
        productDocument.setId(getIntValue(obj, "product_id"));
        productDocument.setName(getStringValue(obj, "name"));
        productDocument.setDescription(getStringValue(obj, "description"));
        productDocument.setReorderLevel(getIntValue(obj, "reorder_level"));
        productDocument.setCategory(CategoryDTO.builder()
                .id(getIntValue(obj, "product_category_id"))
                .build());
        return productDocument;
    }
}
