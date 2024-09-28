package com.tanle.e_commerce.mapper.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.tanle.e_commerce.entities.Option;
import com.tanle.e_commerce.entities.Product;
import com.tanle.e_commerce.entities.SKU;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProductSerializer extends StdSerializer<Product> {
    public ProductSerializer() {
        this(null);
    }
    protected ProductSerializer(Class<Product> t) {
        super(t);
    }

    @Override
    public void serialize(Product product, JsonGenerator jsonGenerator
            , SerializerProvider serializerProvider) throws IOException {
        List<SKU> skus = product.getSkus();
        int stock = skus.stream()
                .mapToInt(SKU::getStock)
                .sum();
        double price = skus.stream()
                .mapToDouble(SKU::getPrice)
                .min()
                .getAsDouble();
        Map<String,List<Option>> options =  product.getOptions()
                .stream()
                .collect(Collectors.groupingBy(s->s.getName()));
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id",product.getId());
        jsonGenerator.writeStringField("name",product.getName());
        jsonGenerator.writeObjectField("stock",stock);
        jsonGenerator.writeObjectField("price",price);
        jsonGenerator.writeObjectField("category",product.getCategory());
        jsonGenerator.writeStringField("description",product.getDescription());
        jsonGenerator.writeNumberField("reorderLevel",product.getReorderLevel());
        jsonGenerator.writeStringField("createAt", product.getCreatedAt().toString());
        jsonGenerator.writeObjectField("options",options);
        jsonGenerator.writeEndObject();

    }
}
