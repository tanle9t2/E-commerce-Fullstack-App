package com.tanle.e_commerce.mapper.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.tanle.e_commerce.entities.Category;

import java.io.IOException;

public class CategorySerializer extends StdSerializer<Category> {
    public CategorySerializer() {
        this(null);
    }
    protected CategorySerializer(Class<Category> t) {
        super(t);
    }

    @Override
    public void serialize(Category category
            , JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("category_id", category.getId());
        jsonGenerator.writeStringField("description", category.getDescription());
        jsonGenerator.writeStringField("name", category.getName());
        jsonGenerator.writeObjectField("createAt", category.getCreateAt());
        jsonGenerator.writeEndObject();
    }

}
