package com.tanle.e_commerce.service.serviceimpl;

import com.tanle.e_commerce.Repository.Jpa.CartItemRepository;
import com.tanle.e_commerce.Repository.Jpa.CartRepository;
import com.tanle.e_commerce.Repository.Jpa.SKURepository;
import com.tanle.e_commerce.Repository.Jpa.UserRepository;
import com.tanle.e_commerce.dto.CartDTO;
import com.tanle.e_commerce.entities.*;
import com.tanle.e_commerce.entities.CompositeKey.CartItemKey;
import com.tanle.e_commerce.exception.ResourceDeleteException;
import com.tanle.e_commerce.exception.ResourceNotFoundExeption;
import com.tanle.e_commerce.respone.MessageResponse;
import com.tanle.e_commerce.service.CartService;
import com.tanle.e_commerce.utils.Patcher;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private SKURepository skuRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CartDTO findById(Integer id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found cart"));
        return cart.converDTO();
    }
    @Override
    @Transactional
    public CartDTO createCart(CartDTO cart) {
        Cart source = modelMapper.map(cart, Cart.class);
        return null;
    }

    @Override
    @Transactional
    public CartDTO updateCart(Cart cart) throws Exception {
        Cart cartdb = cartRepository.findById(cart.getId())
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found cart"));
        if (cart.getUser() != null) {
            User userDb = userRepository.findById(cart.getUser().getId())
                    .orElseThrow(() -> new ResourceNotFoundExeption("Not found user"));
            cart.setUser(userDb);
        }
        Patcher.patch(cartdb, cart);
        cartRepository.save(cartdb);
        return cartdb.converDTO();
    }

    @Override
    @Transactional
    public void deleteCart(Integer id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found cart need to be deleted"));
        cartRepository.delete(cart);
    }

    @Override
    @Transactional
    public MessageResponse addCartItem(Integer cartId, Map<String, Integer> cartItem) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found cart"));

        if (cartItem != null) {
            SKU sku = skuRepository.findById(cartItem.get("skuId"))
                    .orElseThrow(() -> new ResourceNotFoundExeption("Not found sku"));

            Optional<CartItem> cartItemOptional = cart.getCartItems().stream()
                    .filter(c -> c.getSku().getId() == sku.getId())
                    .findFirst();
            CartItem cartItemDB = cartItemOptional.orElse(new CartItem());
            cartItemDB.setSku(sku);
            cartItemDB.setQuantity(cartItem.get("quantity") + cartItemDB.getQuantity());
            cartItemDB.setCreateAt(LocalDateTime.now());

            if (!cartItemOptional.isPresent()) {
                if (!cart.addCartItem(cartItemDB)) {
                    throw new ResourceNotFoundExeption("Add cart item failed!!!");
                }
            }
            cartRepository.save(cart);
        }
        Map<String, Object> mp = new HashMap<>();
        mp.put("cartId", cart.getId());
        mp.put("current item", cart.getCartItems().size());

        return MessageResponse.builder()
                .message("Successfully add product")
                .data(mp)
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    @Transactional
    public MessageResponse updateCartItem(Integer cartId, Integer oldSKU ,Map<String, Integer> cartItem) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found cart"));
        CartItem cartItemDB = cart.getCartItems().stream()
                .filter(c -> c.getSku().getId() == oldSKU)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found cart item"));
        if(cartItem.get("quantity") != null) {
            cartItemDB.setQuantity(cartItem.get("quantity"));
        }
        if(cartItem.get("skuId") != null) {
            SKU sku = skuRepository.findById(cartItem.get("skuId"))
                    .orElseThrow(() -> new ResourceNotFoundExeption("Not found sku"));

            //new cart item
            CartItem newCartItem = new CartItem();
            newCartItem.setSku(sku);
            newCartItem.setCreateAt(LocalDateTime.now());
            newCartItem.setQuantity(cartItemDB.getQuantity());

            cart.addCartItem(newCartItem);

            //remove old cart item
            cartItemRepository.delete(cartItemDB);
            cart.deleteCartItem(cartItemDB);
        }
        cartRepository.save(cart);
        return MessageResponse.builder()
                .message("Successfully update cart item")
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    @Transactional
    public MessageResponse deleteCartItem(Integer cartId, CartItemKey cartItemKey) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found cart"));
        CartItem cartItem = cartItemRepository.findById(cartItemKey)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found cart item"));
        if (!cart.deleteCartItem(cartItem)) {
            throw new ResourceDeleteException("Something wrong!!!!");
        }
        cartItemRepository.delete(cartItem);

        return MessageResponse.builder()
                .status(HttpStatus.OK)
                .message("Successfully delete cart item")
                .data(cartItem.converDTO())
                .build();
    }

}
