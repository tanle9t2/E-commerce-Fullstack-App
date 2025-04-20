package com.tanle.e_commerce.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;


@Entity
@Table(name = "inventory")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@DynamicUpdate
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_inventory_id")
    private int id;
    @Column(name = "quantity")
    private int quantity;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public boolean increaseQuantity(int number) {
        if(number > 0) {
            int newQuantity = this.quantity + number;
            setQuantity(newQuantity);
            return true;
        }
        return false;
    }
    public boolean decreaseQuantity(int number) {
        if(number > 0 && this.quantity - number >=0) {
            int newQuantity = this.quantity - number;
            setQuantity(newQuantity);
            return true;
        }
        return false;
    }

}
