package com.tanle.e_commerce.mapper;

import com.tanle.e_commerce.dto.PaymentDTO;
import com.tanle.e_commerce.entities.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses = {OrderMapper.class})
public interface PaymentMapper {
    @Mapping(target = "userId", source = "order.myUser.id")
    @Mapping(target = "email", source = "order.myUser.email")
    PaymentDTO convertDTO(Payment payment);

    Payment convertEntity(PaymentDTO paymentDTO);
}
