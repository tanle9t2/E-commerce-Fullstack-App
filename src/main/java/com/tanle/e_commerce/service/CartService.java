package com.tanle.e_commerce.service;

import com.tanle.e_commerce.dto.CartDTO;
import com.tanle.e_commerce.dto.CartItemDTO;
import com.tanle.e_commerce.entities.Cart;
import com.tanle.e_commerce.entities.CartItem;
import com.tanle.e_commerce.entities.CompositeKey.CartItemKey;
import com.tanle.e_commerce.entities.Product;
import com.tanle.e_commerce.payload.MessageResponse;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Objects;

public interface CartService {
    CartDTO findById(Integer id);

    CartDTO createCart(CartDTO cart);

    CartDTO updateCart(Cart cart) throws Exception;

    void deleteCart(Integer id);

    MessageResponse addCartItem(Integer cartId, Map<String, Integer> cartItem);
    MessageResponse updateCartItem(Integer cartId, Integer oldSKU ,Map<String,Integer> cartItem);

    MessageResponse deleteCartItem(Integer cartId, CartItemKey cartItemKey);
}
