package com.tanle.e_commerce.mapper;

import com.tanle.e_commerce.dto.OrderDetailDTO;
import com.tanle.e_commerce.entities.Order;
import com.tanle.e_commerce.entities.OrderDetail;
import com.tanle.e_commerce.mapper.decoratormapper.OrderDetailMapperDecorator;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = SKUMapper.class)
@DecoratedWith(OrderDetailMapperDecorator.class)
public interface OrderDetailMapper {
    @Mapping(target = "createdAt", source = "id.createdAt")
    OrderDetailDTO converDTO(OrderDetail orderDetail);
    OrderDetail convertEntity(OrderDetailDTO orderDetailDTO);
}
