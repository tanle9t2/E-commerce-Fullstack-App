package com.tanle.e_commerce.mapper;

import com.tanle.e_commerce.dto.CartItemDTO;
import com.tanle.e_commerce.entities.CartItem;
import com.tanle.e_commerce.mapper.decoratormapper.CartItemMapperDecorator;
import com.tanle.e_commerce.mapper.decoratormapper.ProductMapperDecorator;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
@DecoratedWith(CartItemMapperDecorator.class)
public interface CartItemMapper {

    CartItem convertEntity(CartItemDTO cartItemDTO);
    @Mapping(target = "skuId",source = "cartItem.sku.id")
    @Mapping(target = "skuNo", source = "cartItem.sku.skuNo")
//    @Mapping(target = "modelName", source = "cartItem.sku.getModalName")
    @Mapping(target = "sellPrice", source = "cartItem.sku.price")
    @Mapping(target = "stock", source = "cartItem.sku.stock")
    CartItemDTO convertDTO(CartItem cartItem);


}
