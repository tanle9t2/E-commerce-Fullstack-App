package com.tanle.e_commerce.dto;

import com.tanle.e_commerce.entities.Image;
import com.tanle.e_commerce.entities.Option;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Document(indexName = "product-index")
public class ProductDocument {
    @Id
    @Field(type = FieldType.Integer)
    private Integer id;
    @Field(type = FieldType.Text)
    private String name;
    @Field(type = FieldType.Double)
    private double minPrice;
    @Field(type = FieldType.Double)
    private double maxPrice;
    @Field(type = FieldType.Integer)
    private int reorderLevel;
    @Field(type = FieldType.Double)
    private int stock;
    @Field(type = FieldType.Text)
    private String description;
    @Field(type = FieldType.Date, format = DateFormat.year_month_day)
    private LocalDate createdAt;
    @Field(type = FieldType.Object)
    private CategoryDTO category;
    @Field(type = FieldType.Text)
    private String skuNo;
    @Field(type = FieldType.Object)
    private List<SKUDTO> skus;
    private int totalSell;
    @Field(type = FieldType.Object)
    private TenantDocument tenantDocument;
    private Map<String, Option> options;
    @Field(type = FieldType.Object)
    private List<Image> images;

    @Data
    public static class TenantDocument {
        private int id;
        private String name;
        private String location;
    }
}
