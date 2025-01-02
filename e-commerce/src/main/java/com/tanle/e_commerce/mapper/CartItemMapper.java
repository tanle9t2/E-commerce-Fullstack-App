package com.tanle.e_commerce.mapper;

import com.tanle.e_commerce.dto.CartItemDTO;
import com.tanle.e_commerce.entities.CartItem;
import org.mapstruct.Mapper;

@Mapper
public interface CartItemMapper {

    CartItem convertEntity(CartItemDTO cartItemDTO);
}
