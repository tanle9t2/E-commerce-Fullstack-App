package com.tanle.e_commerce.entities;

import com.tanle.e_commerce.dto.CartDTO;
import com.tanle.e_commerce.dto.CartItemDTO;
import com.tanle.e_commerce.entities.CompositeKey.CartItemKey;
import com.tanle.e_commerce.exception.ResourceNotFoundExeption;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Table(name = "shopping_cart")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shopping_cart_id")
    private int id;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<CartItem> cartItems;

    public CartDTO converDTO() {
        CartDTO cartDTO = new CartDTO();
        CartDTO.GroupCartItemDTO groupCartItemDTO = cartDTO.new GroupCartItemDTO();
        return groupCartItemDTO.builder(this.id,user,cartItems);
    }
    public boolean addCartItem(CartItem cartItem) {
        CartItemKey cartItemKey = new CartItemKey(this.getId(),cartItem.getSku().getId());
        cartItem.setCartItemKey(cartItemKey);
        cartItem.setCart(this);
        return cartItems.add(cartItem);
    }
    public boolean deleteCartItem(CartItem cartItem) {
        cartItem.setCart(null);
        return cartItems.remove(cartItem);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cart cart = (Cart) o;
        return id == cart.id && Objects.equals(user, cart.user) && Objects.equals(cartItems, cart.cartItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, cartItems);
    }
}
