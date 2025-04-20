package com.tanle.e_commerce.controller;

import com.tanle.e_commerce.dto.CartDTO;
import com.tanle.e_commerce.entities.Cart;
import com.tanle.e_commerce.entities.CompositeKey.CartItemKey;
import com.tanle.e_commerce.entities.MyUser;
import com.tanle.e_commerce.respone.MessageResponse;
import com.tanle.e_commerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:5173") // React app URL
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping("/cart/{cartId}")
    public ResponseEntity<CartDTO> getCart(@PathVariable int cartId) {
        CartDTO cartDTO = cartService.findById(cartId);
        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }

    @GetMapping("/cart")
    public ResponseEntity<CartDTO> getCart(@AuthenticationPrincipal MyUser user) {
        CartDTO cartDTO = cartService.findByUsername((user.getUsername()));
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
            @AuthenticationPrincipal MyUser user) {
        MessageResponse cartDTO = cartService.addCartItem(user.getUsername(), cartItem);
        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }

    @PutMapping("/cart/cartItem")
    public ResponseEntity<MessageResponse> updateCartItem(
            @AuthenticationPrincipal MyUser user,
            @RequestBody Map<String, Integer> cartItem) {
        MessageResponse response = cartService.updateCartItem(user.getUsername(),cartItem);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @DeleteMapping("/cart/cartItem")
    public ResponseEntity<MessageResponse> deleteCartItem(
            @AuthenticationPrincipal MyUser user,
            @RequestBody Map<String, List<Integer>> cartItems
    ) {
        MessageResponse messageResponse = cartService.deleteCartItem(user.getUsername(), cartItems.get("cartItems"));
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }
}
