package com.tanle.e_commerce.entities;


import com.fasterxml.jackson.annotation.*;
import com.tanle.e_commerce.dto.ProductDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "product")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@DynamicUpdate
public class Product {
    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "reorder_level")
    private int reorderLevel;
    @Column(name = "description")
    private String description;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_category_id")
    private Category category;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tenant_id")
    private Tenant tenant;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private List<Option> options;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "product")
    @JsonIgnore
    private List<SKU> skus;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
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

    public boolean addSKU(SKU sku) {
        if (this.skus == null) skus = new ArrayList<>();
        sku.setProduct(this);
        return skus.add(sku);
    }

    public void addSKU(List<SKU> listSKU) {
        listSKU.forEach(s -> this.addSKU(s));
    }

    public void removeSKU(SKU sku) {
        sku.setProduct(null);
        skus.remove(sku);
    }

    public void removeSKU() {
        var it = skus.iterator();
        while (it.hasNext()) {
            SKU sku = it.next();
            sku.setProduct(null);
            it.remove();
        }
    }

    public void removeSKU(List<SKU> skuList) {
        skuList.forEach(s -> s.setProduct(null));
        skus.removeAll(skuList);
    }

    public void removeOption(List<Option> optionList) {
        this.options.removeAll(optionList);
    }

    public ProductDTO converDTO() {
        List<SKU> skus = this.getSkus();
        int stock = skus.stream()
                .mapToInt(SKU::getStock)
                .sum();
        double price = skus.stream()
                .mapToDouble(SKU::getPrice)
                .min()
                .orElseGet(() -> 0);
        Map<String, Option> mpOption = new HashMap<>();
        this.options.forEach(o -> mpOption.put(o.getName(), o));
        ProductDTO dto = ProductDTO.builder()
                .id(this.getId())
                .name(this.getName())
                .reorderLevel(this.getReorderLevel())
                .description(this.getDescription())
                .createdAt(this.createdAt.toLocalDate())
                .category(category.convertDTO())
                .stock(stock)
                .price(new double[]{})
//                .options(this.getOptions()
//                        .stream()
//                        .collect(Collectors.groupingBy(Option::getName
//                                ,Collectors.flatMapping(
//                                        option -> option.getOptionValues().stream(),
//                                        Collectors.toList()
//                                ))))

                .options(mpOption)
                .build();
        return dto;
    }

    public double[] getPrice() {
        double minPrice = this.skus.stream()
                .mapToDouble(SKU::getPrice)
                .min()
                .orElseGet(() -> 0);
        double maxPrice = this.skus.stream()
                .mapToDouble(SKU::getPrice)
                .max()
                .orElseGet(() -> 0);

        return new double[]{minPrice, maxPrice};
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id;
    }


    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
