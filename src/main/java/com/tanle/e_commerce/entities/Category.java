package com.tanle.e_commerce.entities;


import com.fasterxml.jackson.annotation.*;
import com.tanle.e_commerce.dto.CategoryDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Entity
@Table(name = "category")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Getter
@Setter
@DynamicUpdate
public class Category {
    @Id
    @Column(name = "product_category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;
    @Column(name = "create_at")
    private LocalDateTime createAt;
    @OneToMany(cascade = CascadeType.ALL
            , mappedBy = "category"
            , fetch = FetchType.LAZY)
//            , orphanRemoval = true)
    private List<Product> products;

    @ManyToOne
    @JoinColumn(name = "tenant_id")
    private Tenant tenant;

    @Column(name = "lft")
    private int left;
    @Column(name = "rgt")
    private int right;

    public boolean addProduct(Product product) {
        if(this.products == null) this.products = new ArrayList<>();
        product.setCategory(this);
        return products.add(product);
    }
    public void addProduct(List<Product> productList) {
        productList.forEach(p -> this.addProduct(p));
    }

    public boolean removeProduct(Product product) {
        product.setCategory(null);
        return products.remove(product);
    }
    public void removeProduct() {
        var it = products.iterator();
        while (it.hasNext()) {
            Product product = it.next();
            product.setCategory(null);
            it.remove();
        }
    }

    public CategoryDTO convertDTO() {
        return CategoryDTO.builder()
                .id(this.id)
                .name(this.name)
                .createAt(this.createAt)
                .description(this.description)
                .left(this.left)
                .right(this.right)
                .build();
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", createAt=" + createAt +
//                ", products=" + products +
                '}';
    }
}
