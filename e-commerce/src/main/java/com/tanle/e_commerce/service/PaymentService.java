package com.tanle.e_commerce.service;

import com.tanle.e_commerce.dto.PaymentDTO;
import com.tanle.e_commerce.entities.Payment;
import com.tanle.e_commerce.respone.PaymentRespone;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.http.HttpRequest;
import java.util.Map;

public interface PaymentService {
    PaymentRespone createPayment(HttpServletRequest request);

    PaymentDTO handlePayment(Map<String, String> params);

}
