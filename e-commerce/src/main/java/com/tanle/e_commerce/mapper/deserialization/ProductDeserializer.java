package com.tanle.e_commerce.mapper.deserialization;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.tanle.e_commerce.dto.CategoryDTO;
import com.tanle.e_commerce.dto.ProductDTO;
import com.tanle.e_commerce.entities.Option;
import com.tanle.e_commerce.entities.OptionValue;

import javax.json.Json;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ProductDeserializer extends StdDeserializer<ProductDTO> {
    public ProductDeserializer() {
        this(null);
    }

    protected ProductDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public ProductDTO deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JacksonException {
        JsonNode jsonNode = jsonParser.getCodec().readTree(jsonParser);
        Integer id = jsonNode.get("id").asInt();
        String name = jsonNode.get("name").asText();
        String description = jsonNode.get("description").asText();
        Integer reorderLevel = jsonNode.get("reorderLevel").asInt();
        String createAtStr = jsonNode.get("createdAt").asText();
        // Parse the String to LocalDateTime
        LocalDate createAt = null;
        if (createAtStr != "null") {
            createAt = LocalDate.parse(createAtStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        double price = jsonNode.get("price").asDouble();
        Integer stock = jsonNode.get("stock").asInt();

        //mapCategory
        JsonNode categoryNode = jsonNode.get("category");
        CategoryDTO categoryDTO = parseCategory(categoryNode);

        //mapOption
        JsonNode optionsNode = jsonNode.get("options");
        Map<String,Option> options = parseOptions(optionsNode);
        
        return ProductDTO.builder()
                .id(id)
                .name(name)
                .description(description)
                .reorderLevel(reorderLevel)
                .createdAt(createAt)
                .stock(stock)
                .price(new double[]{})
                .options(options)
                .category(categoryDTO)
                .build();
    }
    private CategoryDTO parseCategory(JsonNode jsonNode) {
        Integer categoryId = jsonNode.get("id").asInt();
        String categoryDescription =jsonNode.get("description").asText();
        String categoryName = jsonNode.get("name").asText();
        Integer left = jsonNode.get("left").asInt();
        Integer right = jsonNode.get("right").asInt();
        Integer tenantId= jsonNode.get("tenantId").asInt();

        return  CategoryDTO.builder()
                .id(categoryId)
                .name(categoryName)
                .description(categoryDescription)
                .left(left)
                .tenantId(tenantId)
                .right(right)
                .build();

    }
    private Map<String,Option> parseOptions(JsonNode jsonNode) {
        Map<String,Option> options = new HashMap<>();
        if (jsonNode != null) {
            for (Iterator<Map.Entry<String, JsonNode>> it = jsonNode.fields(); it.hasNext(); ) {
                var option1 = it.next();
                JsonNode optionN = option1.getValue();
                Integer optionId = optionN.get("id").asInt();
                String optionName = optionN.get("name").asText();
                JsonNode jsonOptionValue = optionN.get("optionValues");
                List<OptionValue> optionValues = new ArrayList<>();
                if(jsonOptionValue != null && jsonOptionValue.isArray()) {
                    for (JsonNode item :jsonOptionValue) {
                        OptionValue value = new OptionValue(
                                item.get("id").asInt(),
                                item.get("name").asText()
                        );
                        optionValues.add(value);
                    }
                }
                Option option = new Option(optionId,optionName,optionValues);
                options.put(optionName,option);
            }
        }
        return options;
    }
}
