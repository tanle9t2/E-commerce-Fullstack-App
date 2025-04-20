package com.tanle.e_commerce.request;

import com.tanle.e_commerce.dto.OrderDetailDTO;
import com.tanle.e_commerce.entities.OrderDetail;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class OrderCreatedRequest {
    private int addressId;
    private int paymentMethodId;
    private String note;
    private Set<OrderDetailDTO> items;
}
