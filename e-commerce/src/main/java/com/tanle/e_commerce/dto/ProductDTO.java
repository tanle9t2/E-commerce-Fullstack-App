package com.tanle.e_commerce.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tanle.e_commerce.entities.*;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Getter
@Setter
@Document(indexName = "product-index")
public class ProductDTO {
    @Id
    @Field(type = FieldType.Integer)
    private Integer id;
    @Field(type = FieldType.Text)
    private String name;
    @Field(type = FieldType.Double)
    private double price;
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
    private Map<String, Option> options;
    private List<Image> images;

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", reorderLevel=" + reorderLevel +
                ", description='" + description + '\'' +
                ", create_at='" + createdAt + '\'' +
                ", category=" + category +
                '}';
    }

}
