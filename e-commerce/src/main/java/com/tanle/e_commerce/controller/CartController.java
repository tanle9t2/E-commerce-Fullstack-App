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

    @CrossOrigin(origins = "http://localhost:5173") // React app URL
    @DeleteMapping("/cart/cartItem")
    public ResponseEntity<MessageResponse> deleteCartItem(
            @RequestParam(value = "cartId") String cartId,
            @RequestBody Map<String, List<Integer>> cartItems
    ) {
        MessageResponse messageResponse = cartService.deleteCartItem(Integer.parseInt(cartId), cartItems.get("cartItems"));
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }
}
