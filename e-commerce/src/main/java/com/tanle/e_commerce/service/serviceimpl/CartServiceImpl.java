package com.tanle.e_commerce.service.serviceimpl;

import com.tanle.e_commerce.Repository.Jpa.CartItemRepository;
import com.tanle.e_commerce.Repository.Jpa.CartRepository;
import com.tanle.e_commerce.Repository.Jpa.SKURepository;
import com.tanle.e_commerce.Repository.Jpa.UserRepository;
import com.tanle.e_commerce.dto.CartDTO;
import com.tanle.e_commerce.entities.*;
import com.tanle.e_commerce.exception.ResourceDeleteException;
import com.tanle.e_commerce.exception.ResourceNotFoundExeption;
import com.tanle.e_commerce.mapper.CartMapper;
import com.tanle.e_commerce.respone.MessageResponse;
import com.tanle.e_commerce.service.CartService;
import com.tanle.e_commerce.utils.Patcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
    private CartMapper cartMapper;

    @Override
    public CartDTO findById(Integer id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found cart"));
        return cart.converDTO();
    }

    @Override
    public CartDTO findByUserid(Integer cartId) {
        Cart cart = cartRepository.findByUserId(cartId)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found cart of user"));
        return cartMapper.convertDTO(cart);
    }

    @Override
    @Transactional
    public CartDTO createCart(Integer userId) {
        MyUser myUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found user"));
        Cart cart = Cart.builder()
                .myUser(myUser)
                .build();
        cartRepository.save(cart);
        return cartMapper.convertDTO(cart);
    }

    @Override
    @Transactional
    public CartDTO updateCart(Cart cart) throws Exception {
        Cart cartdb = cartRepository.findById(cart.getId())
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found cart"));
        if (cart.getMyUser() != null) {
            MyUser myUserDb = userRepository.findById(cart.getMyUser().getId())
                    .orElseThrow(() -> new ResourceNotFoundExeption("Not found user"));
            cart.setMyUser(myUserDb);
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
//            if (!cartItemOptional.isEmpty())
//                throw new ResourceNotFoundExeption("Product have already exist");
            CartItem cartItemDB = cartItemOptional.orElse(new CartItem());
            cartItemDB.setSku(sku);
            cartItemDB.setQuantity(cartItem.get("quantity") + cartItemDB.getQuantity());
            cartItemDB.setCreateAt(LocalDateTime.now());
            if (!cart.addCartItem(cartItemDB)) {
                throw new ResourceNotFoundExeption("Add cart item failed!!!");
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
    public MessageResponse updateCartItem(Map<String, Integer> cartItem) {
        Cart cart = cartRepository.findById(cartItem.get("cartId"))
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found cart"));
        CartItem cartItemDB = cart.getCartItems().stream()
                .filter(c -> c.getSku().getId() == cartItem.get("skuId"))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found cart item"));

        if (cartItem.get("quantity") != null) {
            cartItemDB.setQuantity(cartItem.get("quantity"));
        }
        cartRepository.save(cart);
        Map<String, Object> mp = new HashMap<>();
        mp.put("cartId", cart.getId());
        mp.put("current item", cart.getCartItems().size());
        return MessageResponse.builder()
                .message("Successfully update cart item")
                .status(HttpStatus.OK)
                .data(mp)
                .build();
    }

    @Override
    @Transactional
    public MessageResponse deleteCartItem(Integer cartId, List<Integer> skuId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found cart"));
        List<CartItem> cartItems = cart.getCartItems().stream()
                .filter(c ->skuId.contains(c.getSku().getId()))
                .collect(Collectors.toList());
        cartItems.forEach(cartItem -> {
            if (!cart.deleteCartItem(cartItem)) {
                throw new ResourceDeleteException("Something wrong!!!!");
            }
        });
        cartItemRepository.deleteAll(cartItems);
        return MessageResponse.builder()
                .status(HttpStatus.OK)
                .message("Successfully delete cart item")
                .build();
    }
    @Override
    public boolean userOwnEntity(Integer integer, String username) {
        Cart cart = cartRepository.findById(integer)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found cart"));

        return cart.getMyUser().getUsername().equals(username);
    }
}
