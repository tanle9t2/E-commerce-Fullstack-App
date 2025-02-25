package com.tanle.e_commerce.service;

import com.tanle.e_commerce.entities.PaymentMethod;

import java.util.Set;

public interface PaymentMethodService {
    Set<PaymentMethod> getAllPaymentMethod();
    PaymentMethod getPaymentMethodById(int id);
}
