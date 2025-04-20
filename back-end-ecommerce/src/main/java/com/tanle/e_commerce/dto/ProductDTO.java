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

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ProductDTO {

    private Integer id;

    private String name;

    private double minPrice;

    private double maxPrice;

    private int reorderLevel;

    private int stock;

    private String description;

    private LocalDate createdAt;

    private CategoryDTO category;

    private String skuNo;

    private List<SKUDTO> skus;
    private int totalSell;

    private int tenantId;
    private Map<String, Option> options;

    private List<Image> images;


}
