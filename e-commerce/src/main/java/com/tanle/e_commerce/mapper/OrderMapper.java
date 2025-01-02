package com.tanle.e_commerce.mapper;

import com.tanle.e_commerce.dto.OrderDTO;
import com.tanle.e_commerce.dto.SKUDTO;
import com.tanle.e_commerce.entities.Order;
import com.tanle.e_commerce.entities.OrderDetail;
import com.tanle.e_commerce.entities.SKU;
import com.tanle.e_commerce.entities.enums.StatusOrder;
import com.tanle.e_commerce.mapper.decoratormapper.OrderMapperDecorator;
import com.tanle.e_commerce.utils.Status;
import org.aspectj.weaver.ast.Or;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ValueMapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {AddressMapper.class, OrderDetailMapper.class})
@DecoratedWith(OrderMapperDecorator.class)
public interface OrderMapper {

    @Mapping(target = "tenantId", source = "tenant.id")
    @Mapping(target = "receiptAddress", source = "address")
    @Mapping(target = "itemList",source = "orderDetails")
    OrderDTO convertDTO(Order order);

    Order convertEntity(OrderDTO orderDTO);

    @ValueMapping(target = "COMPLETE",source = "COMPLETE")
    @ValueMapping(target = "PROCESSING",source = "PROCESSING")
    @ValueMapping(target = "AWAITING_PAYMENT",source = "AWAITING_PAYMENT")
    @ValueMapping(target = "CANCELED",source = "CANCELED")
    @ValueMapping(target = "CANCELLATION_REQUEST",source = "CANCELLATION_REQUEST")
    StatusOrder stringToStatusOrder(String source);



}
