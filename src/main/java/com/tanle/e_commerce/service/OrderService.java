package com.tanle.e_commerce.service;

import com.tanle.e_commerce.dto.OrderDTO;
import com.tanle.e_commerce.entities.Order;
import com.tanle.e_commerce.entities.Tenant;
import com.tanle.e_commerce.payload.MessageResponse;
import com.tanle.e_commerce.payload.PageResponse;
import com.tanle.e_commerce.request.OrderRequest;
import com.tanle.e_commerce.request.SearchRequest;
import com.tanle.e_commerce.service.authorization.OwnerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface OrderService extends OwnerService<Order,Integer> {
    OrderDTO createOrder(OrderDTO orderDTO);

    MessageResponse updateStatusOrder(Map<String,Object> request);
    PageResponse<OrderDTO> getOrders(Integer tenantId, int page, int size);

    MessageResponse handleOrderCancellation(Map<String, Object> request);

    OrderDTO getOrders(int orderId);

    PageResponse<OrderDTO> getOrders(SearchRequest orderRequest);

    PageResponse<OrderDTO> searchOrder(Map<String,String> params, int page ,int size);
}
