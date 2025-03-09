package com.tanle.e_commerce.service;

import com.tanle.e_commerce.dto.PaymentDTO;
import jakarta.servlet.http.HttpSession;

public interface EmailService {
    void sendOtp(String toEmail, HttpSession httpSession);

    boolean verifyOTP(HttpSession session, String otp);

    void sendConfirmPayment(String email, PaymentDTO paymentDTO);
}
