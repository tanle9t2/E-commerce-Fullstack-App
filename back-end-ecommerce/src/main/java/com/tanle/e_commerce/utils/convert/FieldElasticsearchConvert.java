package com.tanle.e_commerce.utils.convert;

import com.google.gson.JsonObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FieldElasticsearchConvert {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    // Utility Methods to handle different types safely
    public static Integer getIntValue(JsonObject obj, String key) {
        return obj.has(key) && !obj.get(key).isJsonNull() ? obj.get(key).getAsInt() : null;
    }

    public static String getStringValue(JsonObject obj, String key) {
        return obj.has(key) && !obj.get(key).isJsonNull() ? obj.get(key).getAsString() : null;
    }

    public static <T extends Enum<T>> T getEnumValue(JsonObject obj, String key, Class<T> enumType) {
        return obj.has(key) && !obj.get(key).isJsonNull() ? Enum.valueOf(enumType, obj.get(key).getAsString()) : null;
    }

    public static Double getDoubleValue(JsonObject obj, String key) {
        return obj.has(key) && !obj.get(key).isJsonNull() ? obj.get(key).getAsDouble() : null;
    }

    public static LocalDateTime getDateTimeValue(JsonObject obj, String key) {
        return obj.has(key) && !obj.get(key).isJsonNull() ? LocalDateTime.parse(obj.get(key).getAsString(), DATE_FORMATTER) : null;
    }
}
