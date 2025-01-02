package com.tanle.e_commerce.controller;

import com.tanle.e_commerce.dto.CartDTO;
import com.tanle.e_commerce.entities.Cart;
import com.tanle.e_commerce.entities.CompositeKey.CartItemKey;
import com.tanle.e_commerce.respone.MessageResponse;
import com.tanle.e_commerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping("/cart/{cartId}")
    public ResponseEntity<CartDTO> getCart(@PathVariable int cartId) {
        CartDTO cartDTO = cartService.findById(cartId);
        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }

    @GetMapping("/cart")
    public ResponseEntity<CartDTO> getCart(@RequestParam(value = "cartId") String cartId) {
        CartDTO cartDTO = cartService.findByUserid(Integer.parseInt(cartId));
        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }

    @PatchMapping("/cart")
    public ResponseEntity<CartDTO> updateCart(@RequestBody Cart cart) throws Exception {
        CartDTO result = cartService.updateCart(cart);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/cart")
    public ResponseEntity<MessageResponse> addProductToCart(
            @RequestBody Map<String, Integer> cartItem,
            @RequestParam("cartId") String cartId) {
        MessageResponse cartDTO = cartService.addCartItem(Integer.parseInt(cartId), cartItem);
        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }

    @PutMapping("/cart/cartItem")
    public ResponseEntity<MessageResponse> updateCartItem(@RequestBody Map<String, Integer> cartItem) {
        MessageResponse response = cartService.updateCartItem(cartItem);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/cart/cartItem")
    public ResponseEntity<MessageResponse> deleteCartItem(
            @RequestBody Map<String, Integer> cartItem
    ) {
        MessageResponse messageResponse = cartService.deleteCartItem(cartItem.get("cartId"), cartItem.get("skuId"));
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }
}
