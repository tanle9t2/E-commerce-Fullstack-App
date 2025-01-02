package com.tanle.e_commerce.entities;

import com.tanle.e_commerce.dto.DiscountDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "discount")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@DynamicUpdate
public class Discount {
    @Id
    @Column(name = "discount_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "percent")
    private double percent;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @Column(name = "expiry")
    private LocalDateTime expiry;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String descroption;

    @Column(name = "quantity")
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    public DiscountDTO converDTO() {
        return DiscountDTO.builder()
                .id(this.id)
                .percent(this.percent)
                .createAt(this.createAt)
                .expiry(this.expiry)
                .name(this.name)
                .expiry(this.expiry)
                .quantity(this.quantity)
                .build();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getExpiry() {
        return expiry;
    }

    public void setExpiry(LocalDateTime expiry) {
        this.expiry = expiry;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescroption() {
        return descroption;
    }

    public void setDescroption(String descroption) {
        this.descroption = descroption;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
