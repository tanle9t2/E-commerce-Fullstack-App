package com.tanle.e_commerce.mapper;

import com.tanle.e_commerce.dto.CartDTO;
import com.tanle.e_commerce.entities.Cart;
import com.tanle.e_commerce.mapper.decoratormapper.CartMapperDecorator;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",uses = {UserMapper.class})
@DecoratedWith(CartMapperDecorator.class)
public interface CartMapper {
    CartDTO convertDTO(Cart cart);
    Cart converEntity(CartDTO cartDTO);
}
