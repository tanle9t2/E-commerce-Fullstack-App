package com.tanle.e_commerce.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiscountDTO {
    private int id;
    private double percent;
    private LocalDateTime createAt;
    private LocalDateTime expiry;
    private String name;
    private String descroption;
    private int quantity;
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
}
