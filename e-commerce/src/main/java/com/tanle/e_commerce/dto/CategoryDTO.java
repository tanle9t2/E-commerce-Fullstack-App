package com.tanle.e_commerce.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tanle.e_commerce.entities.Product;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CategoryDTO {
    private int id;
    private String name;
    private String description;
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
    private LocalDateTime createAt;
    private Integer tenantId;
    private List<Integer> productsId;
    private List<String> pathCategory;
    private int left;
    private int right;
}
