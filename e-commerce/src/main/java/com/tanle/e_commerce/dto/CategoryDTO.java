package com.tanle.e_commerce.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tanle.e_commerce.entities.Product;
import jakarta.persistence.*;
import lombok.*;

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
    private LocalDateTime createAt;
    private Integer tenantId;
    private List<Integer> productsId;
    private List<String> pathCategory;
    private int left;
    private int right;
}
