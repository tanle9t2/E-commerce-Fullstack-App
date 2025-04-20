package com.tanle.e_commerce.dto;

import com.google.gson.annotations.JsonAdapter;
import com.tanle.e_commerce.mapper.deserialization.ProductDeserializer;
import com.tanle.e_commerce.mapper.deserialization.SkuDocumentDeserializer;
import com.tanle.e_commerce.utils.Status;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class SKUDTO {
    private Integer skuId;
    private String modelName;
    private String itemName;
    private String skuNo;
    @Enumerated(EnumType.STRING)
    private Status status;
    private Integer skuStock;
    @Field(type = FieldType.Double)
    private Double skuPrice;
    private LocalDateTime createdAt;
    private List<Integer> optionValueIndex;
}
