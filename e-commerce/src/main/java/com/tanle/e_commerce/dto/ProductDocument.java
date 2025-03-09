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

    @Field(type = FieldType.Text, name = "name")
    private String name;

    @Field(type = FieldType.Double, name = "min_price")
    private double minPrice;

    @Field(type = FieldType.Double, name = "max_price")
    private double maxPrice;

    @Field(type = FieldType.Integer, name = "reorder_level")
    private int reorderLevel;

    @Field(type = FieldType.Double, name = "stock")
    private int stock;

    @Field(type = FieldType.Text, name = "description")
    private String description;

    @Field(type = FieldType.Date, format = DateFormat.year_month_day, name = "created_at")
    private LocalDate createdAt;

    @Field(type = FieldType.Object, name = "category")
    private CategoryDTO category;

    @Field(type = FieldType.Text, name = "sku_no")
    private String skuNo;

    @Field(type = FieldType.Object, name = "skus")
    private List<SKUDTO> skus;

    @Field(type = FieldType.Integer, name = "total_sell")
    private int totalSell;

    @Field(type = FieldType.Object, name = "tenant_document")
    private TenantDocument tenantDocument;

    @Field(type = FieldType.Object, name = "options")
    private Map<String, Option> options;

    @Field(type = FieldType.Object, name = "images")
    private List<Image> images;
    @Data
    public static class TenantDocument {
        private int id;
        private String name;
        private String location;
    }
}
