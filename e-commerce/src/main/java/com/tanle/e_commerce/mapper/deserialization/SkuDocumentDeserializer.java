package com.tanle.e_commerce.mapper.deserialization;

import com.google.gson.*;
import com.tanle.e_commerce.dto.SKUDTO;
import com.tanle.e_commerce.entities.Product;
import com.tanle.e_commerce.utils.Status;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.tanle.e_commerce.utils.convert.FieldElasticsearchConvert.*;

public class SkuDocumentDeserializer implements JsonDeserializer<SKUDTO> {


    @Override
    public SKUDTO deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        SKUDTO skudto = new SKUDTO();

        // Extracting fields safely
        skudto.setSkuId(getIntValue(obj, "sku_id"));
        skudto.setSkuName(getStringValue(obj, "sku_name"));
        skudto.setSkuNo(getStringValue(obj, "sku_no"));
        skudto.setStatus(getEnumValue(obj, "status", Status.class));
        skudto.setSkuStock(getIntValue(obj, "sku_stock"));
        skudto.setSkuPrice(getDoubleValue(obj, "sku_price"));
        return skudto;
    }

    private List<Integer> getListOfIntegers(JsonObject obj, String key) {
        if (obj.has(key) && obj.get(key).isJsonArray()) {
            JsonArray array = obj.getAsJsonArray(key);
            return IntStream.range(0, array.size())
                    .mapToObj(i -> array.get(i).getAsInt())
                    .collect(Collectors.toList());
        }
        return null;
    }
}
