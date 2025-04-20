package com.tanle.e_commerce.mapper.deserialization;

import com.google.gson.*;
import com.tanle.e_commerce.dto.CategoryDTO;
import com.tanle.e_commerce.dto.SKUDTO;

import java.lang.reflect.Type;
import static com.tanle.e_commerce.utils.convert.FieldElasticsearchConvert.*;
public class CategoryDocumentDeserializer implements JsonDeserializer<CategoryDTO> {
    @Override
    public CategoryDTO deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject obj = jsonElement.getAsJsonObject();
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(getIntValue(obj,"product_category_id"));
        categoryDTO.setName(getStringValue(obj,"name"));
        categoryDTO.setLeft(getIntValue(obj,"lft"));
        categoryDTO.setRight(getIntValue(obj,"rgt"));
        categoryDTO.setTenantId(getIntValue(obj,"tenant_id"));

        return categoryDTO;
    }
}
