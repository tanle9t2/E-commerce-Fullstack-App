package com.tanle.e_commerce.service;

import jakarta.servlet.http.HttpSession;

public interface EmailService {
    void sendOtp(String toEmail, HttpSession httpSession);
    boolean verifyOTP(HttpSession session, String otp);

    void sendEmail(String email);
}
