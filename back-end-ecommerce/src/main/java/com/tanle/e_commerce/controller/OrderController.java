package com.tanle.e_commerce.controller;

import com.tanle.e_commerce.dto.OrderDTO;
import com.tanle.e_commerce.entities.MyUser;
import com.tanle.e_commerce.entities.Order;
import com.tanle.e_commerce.request.OrderCreatedRequest;
import com.tanle.e_commerce.respone.MessageResponse;
import com.tanle.e_commerce.respone.PageResponse;
import com.tanle.e_commerce.request.SearchRequest;
import com.tanle.e_commerce.service.OrderService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tanle.e_commerce.utils.AppConstant.*;

@RestController
@RequestMapping("/api/v1")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/order")
    public ResponseEntity<PageResponse<OrderDTO>> getOrders(
            @RequestParam(name = "page", required = false, defaultValue = PAGE_DEFAULT) String page,
            @RequestParam(name = "pageSize", required = false, defaultValue = PAGE_SIZE) String pageSize,
            @RequestParam(name = "orderId", required = false) String orderId,
            @RequestParam(name = "startDate", required = false) String startDate,
            @RequestParam(name = "endDate", required = false) String endDate,
            @RequestParam(name = "paymentId", required = false) String paymentId,
            @RequestParam(name = "username", required = false) String username,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "order", required = false) String order,
            @RequestParam(name = "tenantId") String tenantId
    ) throws BadRequestException {
        Map<String, String> mp = new HashMap<>();
        mp.put("orderId", orderId);
        mp.put("startDate", startDate);
        mp.put("endDate", endDate);
        mp.put("paymentId", paymentId);
        mp.put("username", username);
        mp.put("sortBy", sortBy);
        mp.put("order", order);
        mp.put("status", status);
        mp.put("tenantId", tenantId);
        PageResponse<OrderDTO> response = orderService.searchOrder(mp, Integer.parseInt(page), Integer.parseInt(pageSize));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/orderList")
    public ResponseEntity<PageResponse<OrderDTO>> getOrders(
            @RequestBody SearchRequest orderRequest
    ) {
        PageResponse<OrderDTO> response = orderService.getOrders(orderRequest);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable int orderId) {
        OrderDTO orderDTO = orderService.getOrders(orderId);
        return new ResponseEntity<>(orderDTO, HttpStatus.OK);
    }

    @GetMapping("/order/purchase")
    public ResponseEntity<PageResponse<OrderDTO>> getOrderByUser(@AuthenticationPrincipal MyUser user
            , @RequestParam(name = "type", required = false) String type
            , @RequestParam(name = "page", required = false, defaultValue = PAGE_DEFAULT) String page
            , @RequestParam(name = "size", required = false, defaultValue = PAGE_SIZE) String size) {
        PageResponse<OrderDTO> orderDTOS = orderService.getPurchaseUser(user.getUsername(), type
                , Integer.parseInt(page), Integer.parseInt(size));
        return ResponseEntity.status(HttpStatus.OK).body(orderDTOS);
    }

    @PostMapping("/order")
    public ResponseEntity<MessageResponse> createOrder(
            @AuthenticationPrincipal MyUser user
            ,@RequestBody List<OrderCreatedRequest> orderDTO) {
        MessageResponse response = orderService.createOrder(user,orderDTO);
        return ResponseEntity.ok(response);

    }


    @PutMapping("/order/status")
    public ResponseEntity<MessageResponse> updateStatusOrder(@RequestBody Map<String, Object> request) {
        MessageResponse messageResponse = orderService.updateStatusOrder(request);
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    @PostMapping("/order/cancelOrder")
    public ResponseEntity<MessageResponse> cancelOrder(@RequestBody Map<String, Object> request) {
        MessageResponse messageResponse = orderService.handleOrderCancellation(request);
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

}
