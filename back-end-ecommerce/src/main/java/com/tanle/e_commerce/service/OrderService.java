package com.tanle.e_commerce.service;

import com.tanle.e_commerce.dto.OrderDTO;
import com.tanle.e_commerce.entities.MyUser;
import com.tanle.e_commerce.entities.Order;
import com.tanle.e_commerce.request.OrderCreatedRequest;
import com.tanle.e_commerce.respone.MessageResponse;
import com.tanle.e_commerce.respone.PageResponse;
import com.tanle.e_commerce.request.SearchRequest;
import com.tanle.e_commerce.service.authorization.OwnerService;
import org.apache.coyote.BadRequestException;

import java.util.List;
import java.util.Map;

public interface OrderService extends OwnerService<Order,Integer> {
    OrderDTO createOrder(MyUser user,OrderDTO orderDTO);
    MessageResponse createOrder(MyUser user, List<OrderCreatedRequest> orderDTOS);
    MessageResponse updateStatusOrder(Map<String,Object> request);
    PageResponse<OrderDTO> getOrders(Integer tenantId, int page, int size);

    MessageResponse handleOrderCancellation(Map<String, Object> request);

    OrderDTO getOrders(int orderId);

    PageResponse<OrderDTO> getOrders(SearchRequest orderRequest);

    PageResponse<OrderDTO> searchOrder(Map<String,String> params, int page ,int size) throws BadRequestException;

    PageResponse<OrderDTO> getPurchaseUser(String username,String type, int page ,int size);
}
