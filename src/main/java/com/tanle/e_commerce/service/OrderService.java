package com.tanle.e_commerce.service;

import com.tanle.e_commerce.dto.OrderDTO;
import com.tanle.e_commerce.entities.Order;
import com.tanle.e_commerce.respone.MessageResponse;
import com.tanle.e_commerce.respone.PageResponse;
import com.tanle.e_commerce.request.SearchRequest;
import com.tanle.e_commerce.service.authorization.OwnerService;

import java.util.List;
import java.util.Map;

public interface OrderService extends OwnerService<Order,Integer> {
    OrderDTO createOrder(OrderDTO orderDTO);

    MessageResponse updateStatusOrder(Map<String,Object> request);
    PageResponse<OrderDTO> getOrders(Integer tenantId, int page, int size);

    MessageResponse handleOrderCancellation(Map<String, Object> request);

    OrderDTO getOrders(int orderId);

    PageResponse<OrderDTO> getOrders(SearchRequest orderRequest);

    PageResponse<OrderDTO> searchOrder(Map<String,String> params, int page ,int size);

    PageResponse<OrderDTO> getPurchaseUser(Map<String, Integer> request,String type);
}
