package com.tanle.e_commerce.controller;

import com.tanle.e_commerce.dto.PaymentDTO;
import com.tanle.e_commerce.respone.PaymentRespone;
import com.tanle.e_commerce.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;
    @Value("${payment.vnPay.refundUrl}")
    private String vnp_RefundUrl;

    @GetMapping("/payment")
    public ResponseEntity<PaymentRespone> payOrder(HttpServletRequest httpServletRequest) {
        PaymentRespone paymentRespone = paymentService.createPayment(httpServletRequest);
        return ResponseEntity.status(HttpStatus.OK).body(paymentRespone);
    }

    @GetMapping("/vn-pay-callback")
    public ResponseEntity<PaymentDTO> payCallbackHandler(@RequestParam Map<String, String> params) {
        PaymentDTO paymentDTO = paymentService.handlePayment(params);
        return ResponseEntity.status(HttpStatus.OK).body(paymentDTO);
    }


}
