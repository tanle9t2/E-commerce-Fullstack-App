package com.tanle.e_commerce.mapper.decoratormapper;

import com.tanle.e_commerce.dto.CartItemDTO;
import com.tanle.e_commerce.entities.CartItem;
import com.tanle.e_commerce.mapper.CartItemMapper;
import com.tanle.e_commerce.mapper.ProductMapper;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@NoArgsConstructor
public abstract class CartItemMapperDecorator implements CartItemMapper {
    @Autowired
    private CartItemMapper delegate;
    @Autowired
    private ProductMapper productMapper;
    @Override
    public CartItemDTO convertDTO(CartItem cartItem) {
        CartItemDTO cartItemDTO = delegate.convertDTO(cartItem);
        cartItemDTO.setModelName(cartItem.getSku().getModalName());
        cartItemDTO.setProduct(productMapper.asInput(cartItem.getSku().getProduct()));
        return cartItemDTO;
    }
}
