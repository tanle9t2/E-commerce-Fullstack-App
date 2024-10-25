package com.tanle.e_commerce.respone;

import com.tanle.e_commerce.dto.PaymentDTO;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PaymentRespone {
    private PaymentDTO paymentDTO;
    public String code;
    public String message;
    public String paymentUrl;
}
