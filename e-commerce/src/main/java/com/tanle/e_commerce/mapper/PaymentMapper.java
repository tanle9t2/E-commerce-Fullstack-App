package com.tanle.e_commerce.mapper;

import com.tanle.e_commerce.dto.PaymentDTO;
import com.tanle.e_commerce.entities.Payment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    PaymentDTO convertDTO(Payment payment);
    Payment convertEntity(PaymentDTO paymentDTO);
}
