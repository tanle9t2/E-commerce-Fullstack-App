package com.tanle.e_commerce.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.tanle.e_commerce.dto.ProductDTO;
import com.tanle.e_commerce.entities.Product;
import com.tanle.e_commerce.mapper.deserialization.ProductDeserializer;
import com.tanle.e_commerce.mapper.serialization.ProductSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class ObjectMapperConfig {
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        SimpleModule module = new SimpleModule();
//        module.addSerializer(Product.class,new ProductSerializer());
        module.addDeserializer(ProductDTO.class, new ProductDeserializer());
        // Register custom serializer for LocalDateTime
        javaTimeModule.addSerializer(LocalDateTime.class,
                new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));

        mapper.registerModule(javaTimeModule);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.registerModule(module);
        return mapper;
    }
}
