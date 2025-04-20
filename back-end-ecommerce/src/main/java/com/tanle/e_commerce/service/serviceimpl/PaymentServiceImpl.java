package com.tanle.e_commerce.service.serviceimpl;

import com.tanle.e_commerce.Repository.Jpa.OrderJpaRepository;
import com.tanle.e_commerce.Repository.Jpa.PaymentRepository;
import com.tanle.e_commerce.config.payment.VNPayConfig;
import com.tanle.e_commerce.dto.PaymentDTO;
import com.tanle.e_commerce.entities.Order;
import com.tanle.e_commerce.entities.Payment;
import com.tanle.e_commerce.entities.enums.StatusOrder;
import com.tanle.e_commerce.entities.enums.StatusPayment;
import com.tanle.e_commerce.event.PaymentEvent;
import com.tanle.e_commerce.event.PaymentStatus;
import com.tanle.e_commerce.exception.ResourceNotFoundExeption;
import com.tanle.e_commerce.kafka.KafkaPublisher;
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
import java.util.stream.Collectors;

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
    @Autowired
    private KafkaPublisher publisher;

    @Override
    @Transactional
    public PaymentRespone createPayment(HttpServletRequest request) {
        String bankCode = request.getParameter("bankCode");
        List<String> orderIds = Collections.singletonList(request.getParameter("orderId"));
        long amount = orderIds.stream()
                .mapToLong(o -> {
                    Order order = orderJpaRepository.findById(Integer.parseInt(o))
                            .orElseThrow(() -> new ResourceNotFoundExeption("Not found order"));
                    return order.getOrderDetails()
                            .stream()
                            .mapToLong(od -> (long) (od.getQuantity() * od.getSku().getPrice()))
                            .sum() * 100L;
                })
                .sum();
        String ids = orderIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig(ids);
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
    public PaymentRespone handlePayment(Map<String, String> params) {
        String status = params.get("vnp_ResponseCode");
        if (!verifySignature(params)) {
            throw new IllegalStateException("Signature verification failed");
        }
        PaymentRespone paymentRespone = new PaymentRespone();
        paymentRespone.setCode(status);
        List<String> orderIds = Arrays.stream(params.get("vnp_TxnRef").split(",")).toList();
        if (status.equals("00")) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            LocalDateTime dateTime = LocalDateTime.parse(params.get(VNPayParams.PAY_DATE), formatter);
            orderIds.forEach(o -> {
                Order order = orderJpaRepository.findById(Integer.parseInt(params.get("vnp_TxnRef")))
                        .orElseThrow(() -> new ResourceNotFoundExeption("Not found order"));
                Payment payment = Payment.builder()
                        .status(StatusPayment.SUCCESS)
                        .amount(Double.parseDouble(params.get("vnp_Amount")) / 100)
                        .createdAt(dateTime)
                        .order(order)
                        .build();
                paymentRepository.save(payment);
                PaymentEvent event = PaymentEvent.builder()
                        .paymentRequestDto(paymentMapper.convertDTO(payment))
                        .paymentStatus(PaymentStatus.PAYMENT_COMPLETED)
                        .build();
                publisher.sendPaymentConfirmation(event);
            });
            paymentRespone.setMessage("Successfully payment");

        } else {
            orderIds.forEach(o -> {
                Order order = orderJpaRepository.findById(Integer.parseInt(params.get("vnp_TxnRef")))
                        .orElseThrow(() -> new ResourceNotFoundExeption("Not found order"));
                order.setStatus(StatusOrder.AWAITING_PAYMENT);
                orderJpaRepository.save(order);
            });
            paymentRespone.setMessage("Failure payment");
        }
        return paymentRespone;
    }


}
