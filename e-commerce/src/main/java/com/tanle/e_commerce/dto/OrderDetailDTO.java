package com.tanle.e_commerce.dto;

import com.tanle.e_commerce.entities.SKU;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderDetailDTO {

    private int skuId;
    private int productId;
    private int quantity;
    private String variation;
    private String productName;
    private String image;
    private double price;
}
