package com.tanle.e_commerce.controller;

import com.tanle.e_commerce.dto.CartDTO;
import com.tanle.e_commerce.dto.CartItemDTO;
import com.tanle.e_commerce.entities.Cart;
import com.tanle.e_commerce.entities.CartItem;
import com.tanle.e_commerce.entities.CompositeKey.CartItemKey;
import com.tanle.e_commerce.payload.MessageResponse;
import com.tanle.e_commerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class CartController {
    @Autowired
    private CartService cartService;

    @RequestMapping("/cart/{cartId}")
    public ResponseEntity<CartDTO> getCart(@PathVariable int cartId) {
        CartDTO cartDTO = cartService.findById(cartId);
        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }


    @PatchMapping("/cart")
    public ResponseEntity<CartDTO> updateCart(@RequestBody Cart cart) throws Exception {
        CartDTO result = cartService.updateCart(cart);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/cart")
    public ResponseEntity<MessageResponse> addProductToCart(
            @RequestBody Map<String, Integer> cartItem,
            @RequestParam("cartId") String cartId) {
        MessageResponse cartDTO = cartService.addCartItem(Integer.parseInt(cartId), cartItem);
        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }

    @PutMapping("/cart/cartItem")
    public ResponseEntity<MessageResponse> updateCartItem(@RequestBody Map<String, Integer> cartItem,
                                                          @RequestParam("cartId") String cartId,
                                                          @RequestParam("skuId") String skuId) {
        MessageResponse response = cartService.updateCartItem(Integer.parseInt(cartId)
                , Integer.parseInt(skuId), cartItem);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @DeleteMapping("/cart/cartItem")
    public ResponseEntity<MessageResponse> deleteCartItem(
            @RequestParam("cartId") String cartId,
            @RequestBody CartItemKey cartItemKey
    ) {
        MessageResponse messageResponse = cartService.deleteCartItem(Integer.parseInt(cartId),cartItemKey);
        return new ResponseEntity<>(messageResponse,HttpStatus.OK);
    }
}
