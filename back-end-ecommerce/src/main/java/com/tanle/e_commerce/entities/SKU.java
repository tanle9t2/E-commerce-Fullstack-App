package com.tanle.e_commerce.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.tanle.e_commerce.dto.SKUDTO;
import com.tanle.e_commerce.mapper.SKUMapper;
import jakarta.persistence.*;
import lombok.*;

import com.tanle.e_commerce.utils.Status;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "sku")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@DynamicUpdate
public class SKU {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sku_id")
    private int id;

    @Column(name = "sku_name")
    private String skuName;

    @Column(name = "sku_no")
    private String skuNo;

    @Column(name = "sku_description")
    private String description;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "sku_stock")
    private int stock;

    @Column(name = "sku_price")
    private double price;

    @Column(name = "create_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "sku_attribute",
            joinColumns = @JoinColumn(name = "sku_id"),
            inverseJoinColumns = @JoinColumn(name = "option_value_id")
    )
    private List<OptionValue> optionValues;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    public boolean increaseStock(int quantity) {
        if (quantity < 0) return false;
        this.stock += quantity;
        return true;
    }

    public boolean descreaseStock(int quantity) {
        if (quantity < 0 || quantity > stock) return false;
        this.stock -= quantity;
        return true;
    }


    public SKUDTO converDTO() {
        List<Integer> optionValueIndex = new ArrayList<>();
        int i = 0;
        for (Option option : this.product.getOptions()) {
            optionValueIndex.add(option.parseIndexOptionValue(optionValues.get(i)));
            i++;
        }
        return SKUDTO.builder()
                .skuId(this.id)
                .skuNo(this.skuNo)
                .modelName(getModalName())
                .skuPrice(this.price)
                .status(this.status)
                .skuStock(this.stock)
                .optionValueIndex(optionValueIndex)
                .createdAt(this.createAt)
                .build();
    }

    public String getModalName() {
        return optionValues.stream()
                .map(OptionValue::getName)
                .collect(Collectors.joining(","));
    }

    public void setId(int id) {
        this.id = id;
    }
}
