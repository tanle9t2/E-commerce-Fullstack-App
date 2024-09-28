package com.tanle.e_commerce.service.facade;

import com.tanle.e_commerce.dto.CartDTO;
import com.tanle.e_commerce.entities.Cart;
import com.tanle.e_commerce.entities.CartItem;
import com.tanle.e_commerce.entities.Product;
import com.tanle.e_commerce.exception.ResourceNotFoundExeption;
import com.tanle.e_commerce.service.CartService;
import com.tanle.e_commerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartFacadeService {
    @Autowired
    private CartService cartService;
    @Autowired
    private ProductService productService;

    @Transactional
    public ResponseEntity<CartDTO> addProductToCart(Integer cartId, CartItem cartItem) {
//        Cart cart = cartRepository.findById(cartId)
//                .orElseThrow(()-> new ResourceNotFoundExeption("Not found cart"));
//        if(cartItem != null) {
//            cart.addCartItem(cartItem);
//            Product product = cartItem.getProduct();
//            product.getInventory().decreaseQuantity(cartItem.getQuantity());
//            cartRepository.save(cart);
//        }
//        return new ResponseEntity<>(cart.converDTO(), HttpStatus.OK);
        return null;
    }
}
