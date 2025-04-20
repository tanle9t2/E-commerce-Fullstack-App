package com.tanle.e_commerce.service;

import com.tanle.e_commerce.dto.CartDTO;
import com.tanle.e_commerce.entities.Cart;
import com.tanle.e_commerce.entities.CompositeKey.CartItemKey;
import com.tanle.e_commerce.respone.MessageResponse;
import com.tanle.e_commerce.service.authorization.OwnerService;

import java.util.List;
import java.util.Map;

public interface CartService extends OwnerService<Cart,Integer> {
    CartDTO findById(Integer id);
    CartDTO findByUsername(String username);

    CartDTO createCart(Integer userId);

    CartDTO updateCart(Cart cart) throws Exception;

    void deleteCart(Integer id);

    MessageResponse addCartItem(String username, Map<String, Integer> cartItem);
    MessageResponse updateCartItem(String username,Map<String,Integer> cartItem);

    MessageResponse deleteCartItem(String username, List<Integer> skuId);
}
