package com.tanle.e_commerce.service;

import com.tanle.e_commerce.dto.PaymentDTO;
import com.tanle.e_commerce.entities.Payment;
import com.tanle.e_commerce.respone.PaymentRespone;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;

import java.net.http.HttpRequest;
import java.util.Map;

public interface PaymentService {
    public PaymentRespone createPayment(HttpServletRequest request);

    PaymentDTO handlePayment(HttpServletRequest request);
}
