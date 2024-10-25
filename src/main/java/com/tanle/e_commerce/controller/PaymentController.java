package com.tanle.e_commerce.controller;

import com.tanle.e_commerce.dto.PaymentDTO;
import com.tanle.e_commerce.respone.PaymentRespone;
import com.tanle.e_commerce.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @GetMapping("/payment")
    public ResponseEntity<PaymentRespone> payOrder(HttpServletRequest httpServletRequest) {
        PaymentRespone paymentRespone = paymentService.createPayment(httpServletRequest);
        return ResponseEntity.status(HttpStatus.OK).body(paymentRespone);
    }
    @GetMapping("/vn-pay-callback")
    public ResponseEntity<PaymentDTO> payCallbackHandler(HttpServletRequest request) {
        String status = request.getParameter("vnp_ResponseCode");
        if (status.equals("00")) {
            PaymentDTO paymentDTO = paymentService.handlePayment(request);
            return null;
        } else {
            return null;
        }
    }
}
