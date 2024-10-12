package com.tanle.e_commerce.service.serviceimpl;

import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.json.JsonData;
import com.tanle.e_commerce.Repository.Jpa.OrderCancellationRepository;
import com.tanle.e_commerce.Repository.Jpa.OrderJpaRepository;
import com.tanle.e_commerce.Repository.Jpa.TenantRepository;
import com.tanle.e_commerce.Repository.Jpa.UserRepository;
import com.tanle.e_commerce.dto.OrderDTO;
import com.tanle.e_commerce.entities.*;
import com.tanle.e_commerce.entities.enums.StatusOrder;
import com.tanle.e_commerce.exception.ResourceNotFoundExeption;
import com.tanle.e_commerce.mapper.OrderMapper;
import com.tanle.e_commerce.respone.MessageResponse;
import com.tanle.e_commerce.respone.PageResponse;
import com.tanle.e_commerce.request.SearchRequest;
import com.tanle.e_commerce.service.OrderService;
import com.tanle.e_commerce.utils.filter.FilterSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderJpaRepository orderJPARepository;
    @Autowired
    private TenantRepository tenantRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderCancellationRepository orderCancellationRepository;
    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Override
    public OrderDTO getOrders(int orderId) {
        Order order = orderJPARepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found order"));
        return orderMapper.convertDTO(order);
    }

    @Override
    public PageResponse<OrderDTO> getOrders(SearchRequest orderRequest) {
        FilterSpecification specification = new FilterSpecification<>(orderRequest);
        Page<Order> orderPage = orderJPARepository.findAll(specification,orderRequest.getPageable());
        return getResult(orderPage);
    }

    @Override
    public PageResponse<OrderDTO> searchOrder(Map<String, String> params, int page, int size) {
        var builderQuery = QueryBuilders.bool();
        String startDate = params.get("startDate") != null
                ? params.get("startDate")
                : LocalDateTime.of(1000,1,1,1,1,1)
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String endDate = params.get("endDate") != null
                ? params.get("endDate")
                : LocalDateTime.of(9999,12,31,12,59,59)
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        if(params.get("orderId") != null) {
            builderQuery.must(builder ->
                    builder.term(t -> t.field("id").value(Integer.parseInt(params.get("orderId")))));
        }
        if(params.get("username") != null) {
            builderQuery.must(builder -> builder.matchPhrase(
                    m -> m.field("userOrder.fullName").query(params.get("username"))));
        }
        if(params.get("status") != null) {
            builderQuery.must(builder -> builder.match(
                    t -> t.field("status").query(params.get("status"))));
        }
        if(params.get("paymentId") != null) {
            builderQuery.must(builder ->
                    builder.term(t -> t.field("paymentId").value(Integer.parseInt(params.get("paymentId")))));
        }
        NativeQuery query = new NativeQueryBuilder()
                .withQuery(builderQuery.build()._toQuery())
                .withFilter(f -> f.range( r-> r.field("createdAt")
                        .lte(JsonData.of(endDate))
                        .gte(JsonData.of(startDate))))
                .withSort(s -> {
                    if(params.get("sortBy") != null && params.get("order") != null) {
                        return s.field( f -> f.field(params.get("sortBy"))
                                .order(params.get("order").equals("asc") ? SortOrder.Asc : SortOrder.Desc));
                    }
                    return s.field(f-> f.field("_score").order(SortOrder.Desc));
                })
                .withPageable(PageRequest.of(page,size))
                .build();

        Long totalOrder =  elasticsearchOperations.count(query,OrderDTO.class);
        List<OrderDTO> orderDTOS = elasticsearchOperations.search(query,OrderDTO.class).stream()
                .map(hits -> hits.getContent())
                .collect(Collectors.toList());
        return new PageResponse<>(orderDTOS,page,orderDTOS.size(),totalOrder,HttpStatus.OK);
    }

    @Override
    public List<OrderDTO> getPurchaseUser(Map<String, Integer> request, String type) {
        List<Order> orders = orderJPARepository.findOrderId(request.get("userIdRequest")
                ,StatusOrder.valueOf(type.toUpperCase()));

        return orders.stream()
                .map(o -> orderMapper.convertDTO(o))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderDTO createOrder(OrderDTO orderDTO) {
        Order order = orderMapper.convertEntity(orderDTO);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(StatusOrder.PROCESSING);
        for (var item : order.getOrderDetails()) {
            SKU sku = item.getSku();
            if( !sku.descreaseStock(item.getQuantity()) ) throw new RuntimeException("Something wrong");
        }
        orderJPARepository.save(order);
        return orderMapper.convertDTO(order);
    }

    @Override
    @Transactional
    public MessageResponse updateStatusOrder(Map<String, Object> request) {
        Order order = orderJPARepository.findById((Integer) request.get("orderId"))
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found order"));
        order.setStatus(StatusOrder.valueOf(((String) request.get("status")).toUpperCase()));
        orderJPARepository.save(order);

        return MessageResponse.builder()
                .status(HttpStatus.OK)
                .message("Successfully update status")
                .data(orderMapper.convertDTO(order))
                .build();
    }

    private PageResponse<OrderDTO> getResult(Page<Order> orders) {
        if (orders.getNumberOfElements() == 0) {
            return new PageResponse<>(Collections.emptyList(),
                    orders.getNumber(), orders.getNumberOfElements()
                    , orders.getTotalElements(), HttpStatus.OK);
        }
        List<OrderDTO> orderDTOs = orders.getContent().stream()
                .map(o -> orderMapper.convertDTO(o))
                .collect(Collectors.toList());
        return new PageResponse<>(orderDTOs, orders.getNumber(), orders.getNumberOfElements()
                , orders.getTotalElements(), HttpStatus.OK);
    }

    @Override
    public PageResponse<OrderDTO> getOrders(Integer tenantId, int page, int size) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found tenant"));
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orderPage = orderJPARepository.findOrderByTenant(tenant, pageable);
        return getResult(orderPage);
    }

    @Override
    @Transactional
    public MessageResponse handleOrderCancellation(Map<String, Object> request) {
        Order order = orderJPARepository.findById((Integer) request.get("orderId"))
                .orElseThrow(() -> new RuntimeException("Not found order"));

        if (order.getStatus() != StatusOrder.CANCELLATION_REQUEST ||
                order.getTenant().getId() != (Integer) request.get("tenantIdRequest"))
            throw new RuntimeException("Something wrong");

        for (var item : order.getOrderDetails()) {
            SKU sku = item.getSku();
            if( !sku.increaseStock(item.getQuantity()) ) throw new RuntimeException("Something wrong");
        }
        order.setStatus(StatusOrder.CANCELED);

//        OrderCancellation orderCancellation = new OrderCancellation(order,LocalDateTime.now()
//                ,(String) request.get("reason"),"ACCEPT") ;
        OrderCancellation orderCancellation = OrderCancellation.builder()
                .canceledAt(LocalDateTime.now())
                .operation("ACCEPT")
                .order(order)
                .reason((String) request.get("reason"))
                .build();
        orderCancellationRepository.save(orderCancellation);
        return MessageResponse.builder()
                .message("Successfully cancel order")
                .status(HttpStatus.OK)
                .build();
    }
    @Override
    public boolean userOwnEntity(Integer integer, String username) {
        Order order = orderJPARepository.findById(integer)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found order"));

        return order.getUser().getUsername().equals(username);
    }
}
