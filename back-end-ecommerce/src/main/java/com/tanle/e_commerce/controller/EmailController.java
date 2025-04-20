package com.tanle.e_commerce.controller;

import com.tanle.e_commerce.exception.UnauthorizedException;
import com.tanle.e_commerce.service.EmailService;
import jakarta.servlet.http.HttpSession;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/email")
@CrossOrigin(origins = "http://localhost:5173") // React app URL
public class EmailController {
    @Autowired
    private EmailService emailService;

    @PostMapping("/sendOtp")
    public ResponseEntity<String> sendOtp(@RequestBody Map<String, String> request, HttpSession httpSession) {
        String toEmail = request.get("toEmail");


        emailService.sendOtp(toEmail, httpSession);
        return ResponseEntity.ok("OTP sent successfully!");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<Map<String, Object>> verifyOtp(@RequestBody Map<String, String> request, HttpSession session) throws BadRequestException {
        String otp = request.get("otp");
        Map<String, Object> response = new HashMap<>();

        if (otp == null || otp.isEmpty()) {
            throw new BadRequestException("OTP is required");
        }

        if (emailService.verifyOTP(session, otp)) {
            response.put("status", "success");
            response.put("message", "OTP verified successfully");
            return ResponseEntity.ok(response);
        } else {
            throw new UnauthorizedException("Invalid or expired OTP");
        }
    }

}
