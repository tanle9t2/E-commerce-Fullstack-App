package com.tanle.e_commerce.service;

import com.tanle.e_commerce.dto.CartDTO;
import com.tanle.e_commerce.entities.Cart;
import com.tanle.e_commerce.entities.CompositeKey.CartItemKey;
import com.tanle.e_commerce.respone.MessageResponse;
import com.tanle.e_commerce.service.authorization.OwnerService;

import java.util.Map;

public interface CartService extends OwnerService<Cart,Integer> {
    CartDTO findById(Integer id);
    CartDTO findByUserid(Map<String, Integer> request);

    CartDTO createCart(Integer userId);

    CartDTO updateCart(Cart cart) throws Exception;

    void deleteCart(Integer id);

    MessageResponse addCartItem(Integer cartId, Map<String, Integer> cartItem);
    MessageResponse updateCartItem(Map<String,Integer> cartItem);

    MessageResponse deleteCartItem(Integer cartId, CartItemKey cartItemKey);
}
