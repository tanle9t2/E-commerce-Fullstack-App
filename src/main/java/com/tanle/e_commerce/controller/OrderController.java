package com.tanle.e_commerce.controller;

import com.tanle.e_commerce.dto.OrderDTO;
import com.tanle.e_commerce.respone.MessageResponse;
import com.tanle.e_commerce.respone.PageResponse;
import com.tanle.e_commerce.request.SearchRequest;
import com.tanle.e_commerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static com.tanle.e_commerce.utils.AppConstant.*;

@RestController
@RequestMapping("/api/v1")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/orders")
    public ResponseEntity<PageResponse<OrderDTO>> getOrders(
            @RequestParam(name = "page", required = false, defaultValue = PAGE_DEFAULT) String page,
            @RequestParam(name = "pageSize", required = false, defaultValue = PAGE_SIZE) String pageSize,
            @RequestParam(name = "order", required = false, defaultValue = DIRECTION_SORT_DEFAULT)String direction,
            @RequestParam(name ="tenantId") String tenantId
    ) {
        PageResponse<OrderDTO> response = orderService.getOrders(Integer.parseInt(tenantId)
                ,Integer.parseInt(page),Integer.parseInt(pageSize));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/orderList2")
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
            @RequestParam(name = "order", required = false) String order
    ) {
        Map<String,String > mp =  new HashMap<>();
        mp.put("orderId",orderId);
        mp.put("startDate",startDate);
        mp.put("endDate",endDate);
        mp.put("paymentId",paymentId);
        mp.put("username",username);
        mp.put("sortBy",sortBy);
        mp.put("order",order);
        mp.put("status",status);

        PageResponse<OrderDTO> response = orderService.searchOrder(mp,Integer.parseInt(page),Integer.parseInt(pageSize));
        return new ResponseEntity<>(response,HttpStatus.OK);
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
        return new ResponseEntity<>(orderDTO,HttpStatus.OK);
    }
    @PostMapping("/order")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) {
        OrderDTO response = orderService.createOrder(orderDTO);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    @PutMapping("/order/status")
    public ResponseEntity<MessageResponse> updateStatusOrder(@RequestBody Map<String, Object> request) {
        MessageResponse messageResponse = orderService.updateStatusOrder(request);
        return new ResponseEntity<>(messageResponse,HttpStatus.OK);
    }

    @PostMapping("/order/cancelOrder")
    public ResponseEntity<MessageResponse> cancelOrder(@RequestBody Map<String, Object> request) {
        MessageResponse messageResponse = orderService.handleOrderCancellation(request);
        return new ResponseEntity<>(messageResponse,HttpStatus.OK);
    }

}
