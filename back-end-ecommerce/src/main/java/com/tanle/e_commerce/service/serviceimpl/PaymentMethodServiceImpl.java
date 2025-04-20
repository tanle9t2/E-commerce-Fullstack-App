package com.tanle.e_commerce.service.serviceimpl;

import com.tanle.e_commerce.Repository.Jpa.PaymentMethodRepository;
import com.tanle.e_commerce.entities.PaymentMethod;
import com.tanle.e_commerce.exception.ResourceNotFoundExeption;
import com.tanle.e_commerce.service.PaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Override
    public Set<PaymentMethod> getAllPaymentMethod() {
        return paymentMethodRepository.findAll().stream()
                .collect(Collectors.toSet());
    }

    @Override
    public PaymentMethod getPaymentMethodById(int id) {
        return paymentMethodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found payment method"));
    }
}
