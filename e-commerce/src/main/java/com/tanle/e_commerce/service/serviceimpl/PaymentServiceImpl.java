package com.tanle.e_commerce.service.serviceimpl;

import com.tanle.e_commerce.Repository.Jpa.OrderJpaRepository;
import com.tanle.e_commerce.Repository.Jpa.PaymentRepository;
import com.tanle.e_commerce.config.payment.VNPayConfig;
import com.tanle.e_commerce.dto.PaymentDTO;
import com.tanle.e_commerce.entities.Order;
import com.tanle.e_commerce.entities.Payment;
import com.tanle.e_commerce.entities.PaymentStatus;
import com.tanle.e_commerce.entities.enums.StatusOrder;
import com.tanle.e_commerce.entities.enums.StatusPayment;
import com.tanle.e_commerce.exception.ResourceNotFoundExeption;
import com.tanle.e_commerce.mapper.PaymentMapper;
import com.tanle.e_commerce.respone.PaymentRespone;
import com.tanle.e_commerce.service.PaymentService;
import com.tanle.e_commerce.utils.payment.VNPayParams;
import com.tanle.e_commerce.utils.payment.VNPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private VNPayConfig vnPayConfig;
    @Autowired
    private OrderJpaRepository orderJpaRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private PaymentMapper paymentMapper;

    @Override
    @Transactional
    public PaymentRespone createPayment(HttpServletRequest request) {
        String bankCode = request.getParameter("bankCode");
        int orderId = Integer.parseInt(request.getParameter("orderId"));
        Order order = orderJpaRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found order"));

        long amount = order.getOrderDetails()
                .stream()
                .mapToLong(o -> (long) (o.getQuantity() * o.getSku().getPrice()))
                .sum() * 100L;

        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig(orderId);
        vnpParamsMap.put("vnp_Amount", String.valueOf(amount));
        if (bankCode != null && !bankCode.isEmpty()) {
            vnpParamsMap.put("vnp_BankCode", bankCode);
        }
        vnpParamsMap.put("vnp_IpAddr", VNPayUtil.getIpAddress(request));
        //build query url
        String queryUrl = VNPayUtil.getPaymentURL(vnpParamsMap, true);
        String hashData = VNPayUtil.getPaymentURL(vnpParamsMap, false);
        String vnpSecureHash = VNPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
        String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;

        return PaymentRespone.builder()
                .code("OK")
                .message("successfully")
                .paymentUrl(paymentUrl)
                .build();

    }

    private boolean verifySignature(Map<String, String> params) {
        var reqSecureHash = params.get(VNPayParams.SECURE_HASH);
        params.remove(VNPayParams.SECURE_HASH);
        params.remove(VNPayParams.SECURE_HASH_TYPE);
        var hashPayload = new StringBuilder();
        var fieldNames = new ArrayList<>(params.keySet());
        Collections.sort(fieldNames);

        var itr = fieldNames.iterator();
        while (itr.hasNext()) {
            var fieldName = itr.next();
            var fieldValue = params.get(fieldName);
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                //Build hash data
                hashPayload.append(fieldName);
                hashPayload.append("=");
                hashPayload.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));

                if (itr.hasNext()) {
                    hashPayload.append("&");
                }
            }
        }
        var secureHash = VNPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashPayload.toString());
        return secureHash.equals(reqSecureHash);
    }


    @Override
    public PaymentDTO handlePayment(Map<String, String> params) {

        String status = params.get("vnp_ResponseCode");
        if (!verifySignature(params)) {
            throw new IllegalStateException("Signature verification failed");
        }
        if (status.equals("00")) {
            Order order = orderJpaRepository.findById(Integer.parseInt(params.get("vnp_TxnRef")))
                    .orElseThrow(() -> new ResourceNotFoundExeption("Not found order"));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            LocalDateTime dateTime = LocalDateTime.parse(params.get(VNPayParams.PAY_DATE), formatter);
            Payment payment = Payment.builder()
                    .status(StatusPayment.SUCCESS)
                    .amount(Double.parseDouble(params.get("vnp_Amount")) / 100)
                    .createdAt(dateTime)

                    .order(order)
                    .build();

            paymentRepository.save(payment);
            return paymentMapper.convertDTO(payment);
        }
        return null;

    }

    public static String buildDataString(Map<String, String> vnpParamsMap) {
        String vnp_RequestId = vnpParamsMap.getOrDefault("vnp_RequestId", "");
        String vnp_Version = vnpParamsMap.getOrDefault("vnp_Version", "");
        String vnp_Command = vnpParamsMap.getOrDefault("vnp_Command", "");
        String vnp_TmnCode = vnpParamsMap.getOrDefault("vnp_TmnCode", "");
        String vnp_TransactionType = vnpParamsMap.getOrDefault("vnp_TransactionType", "");
        String vnp_TxnRef = vnpParamsMap.getOrDefault("vnp_TxnRef", "");
        String vnp_Amount = vnpParamsMap.getOrDefault("vnp_Amount", "");
        String vnp_TransactionNo = vnpParamsMap.getOrDefault("vnp_TransactionNo", "");
        String vnp_TransactionDate = vnpParamsMap.getOrDefault("vnp_TransactionDate", "");
        String vnp_CreateBy = vnpParamsMap.getOrDefault("vnp_CreateBy", "");
        String vnp_CreateDate = vnpParamsMap.getOrDefault("vnp_CreateDate", "");
        String vnp_IpAddr = vnpParamsMap.getOrDefault("vnp_IpAddr", "");
        String vnp_OrderInfo = vnpParamsMap.getOrDefault("vnp_OrderInfo", "");

        // Concatenate values with "|"
        return String.join("|", vnp_RequestId, vnp_Version, vnp_Command, vnp_TmnCode,
                vnp_TransactionType, vnp_TxnRef, vnp_Amount, vnp_TransactionNo,
                vnp_TransactionDate, vnp_CreateBy, vnp_CreateDate, vnp_IpAddr, vnp_OrderInfo);
    }


}
