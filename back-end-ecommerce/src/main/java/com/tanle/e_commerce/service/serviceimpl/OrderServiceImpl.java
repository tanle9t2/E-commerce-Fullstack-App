package com.tanle.e_commerce.service.serviceimpl;

import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.json.JsonData;
import com.tanle.e_commerce.Repository.Jpa.*;
import com.tanle.e_commerce.dto.AddressDTO;
import com.tanle.e_commerce.dto.OrderDTO;
import com.tanle.e_commerce.entities.*;
import com.tanle.e_commerce.entities.enums.StatusOrder;
import com.tanle.e_commerce.exception.ResourceNotFoundExeption;
import com.tanle.e_commerce.mapper.AddressMapper;
import com.tanle.e_commerce.mapper.OrderMapper;
import com.tanle.e_commerce.request.OrderCreatedRequest;
import com.tanle.e_commerce.respone.MessageResponse;
import com.tanle.e_commerce.respone.PageResponse;
import com.tanle.e_commerce.request.SearchRequest;
import com.tanle.e_commerce.service.OrderService;
import com.tanle.e_commerce.utils.filter.FilterSpecification;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.expression.AccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderJpaRepository orderJPARepository;
    @Autowired
    private AddressMapper addressMapper;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private TenantRepository tenantRepository;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserRepository userRepository;
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
        Page<Order> orderPage = orderJPARepository.findAll(specification, orderRequest.getPageable());
        return getResult(orderPage);
    }

    @Override
    public PageResponse<OrderDTO> searchOrder(Map<String, String> params, int page, int size) throws BadRequestException {
        MyUser myUser = userRepository.findById(Integer.valueOf(params.get("tenantId")))
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found user"));
        if (!myUser.getUsername().equals(SecurityContextHolder.getContext().getAuthentication().getName()))
            throw new BadRequestException("NOT PERMISSION");

        Pageable pageable = size != 0 ? PageRequest.of(page, size) : Pageable.unpaged();
        var builderQuery = QueryBuilders.bool();
        String startDate = params.get("startDate") != null
                ? params.get("startDate")
                : LocalDateTime.of(1000, 1, 1, 1, 1, 1)
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String endDate = params.get("endDate") != null
                ? params.get("endDate")
                : LocalDateTime.of(9999, 12, 31, 12, 59, 59)
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        builderQuery.must(builder ->
                builder.term(t -> t.field("tenantId").value(myUser.getId())));
        if (params.get("orderId") != null) {
            builderQuery.must(builder ->
                    builder.term(t -> t.field("id").value(Integer.parseInt(params.get("orderId")))));
        }
        if (params.get("username") != null) {
            builderQuery.must(builder -> builder.matchPhrase(
                    m -> m.field("userOrder.fullName").query(params.get("username"))));
        }
        if (params.get("status") != null) {
            builderQuery.must(builder -> builder.match(
                    t -> t.field("status").query(params.get("status"))));
        }
        if (params.get("paymentId") != null) {
            builderQuery.must(builder ->
                    builder.term(t -> t.field("paymentId").value(Integer.parseInt(params.get("paymentId")))));
        }
        NativeQuery query = new NativeQueryBuilder()
                .withQuery(builderQuery.build()._toQuery())
                .withFilter(f -> f.range(r -> r.field("createdAt")
                        .lte(JsonData.of(endDate))
                        .gte(JsonData.of(startDate))))
                .withSort(s -> {
                    if (params.get("sortBy") != null && params.get("order") != null) {
                        return s.field(f -> f.field(params.get("sortBy"))
                                .order(params.get("order").equals("asc") ? SortOrder.Asc : SortOrder.Desc));
                    }
                    return s.field(f -> f.field("_score").order(SortOrder.Desc));
                })
                .withPageable(pageable)
                .build();

        Long totalOrder = elasticsearchOperations.count(query, OrderDTO.class);
        List<OrderDTO> orderDTOS = elasticsearchOperations.search(query, OrderDTO.class).stream()
                .map(hits -> hits.getContent())
                .collect(Collectors.toList());
        return new PageResponse<>(orderDTOS, page, orderDTOS.size(), totalOrder, HttpStatus.OK);
    }

    @Override
    public PageResponse<OrderDTO> getPurchaseUser(String username, String type, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Order> orders = orderJPARepository.findOrderId(username
                , type != null ? StatusOrder.valueOf(type.toUpperCase()) : null
                , pageable);
        return getResult(orders);
    }

    @Override
    @Transactional
    public OrderDTO createOrder(MyUser user, OrderDTO orderDTO) {
        Order order = orderMapper.convertEntity(orderDTO);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(StatusOrder.PROCESSING);
        for (var item : order.getOrderDetails()) {
            SKU sku = item.getSku();
            if (!sku.descreaseStock(item.getQuantity())) throw new RuntimeException("Something wrong");
        }
        orderJPARepository.save(order);
        return orderMapper.convertDTO(order);
    }

    @Override
    @Transactional
    public MessageResponse createOrder(MyUser user, List<OrderCreatedRequest> orderDTOS) {
        AddressDTO addressDTO = addressMapper.convertDTO(addressRepository.findById(orderDTOS.get(0).getAddressId())
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found address")));
        List<OrderDTO> items = orderDTOS.stream()
                .map(or -> {
                    OrderDTO orderDTO = OrderDTO.builder()
                            .receiptAddress(addressDTO)
                            .note(or.getNote())
                            .paymentMethodId(or.getPaymentMethodId())
                            .itemList(or.getItems())
                            .build();
                    orderDTO.setUserOrder(orderDTO.buildUserOrder(user.getId(), user.getFullName()));
                    return orderDTO;
                })
                .map(or -> createOrder(user, or))
                .collect(Collectors.toList());

        return MessageResponse.builder()
                .status(HttpStatus.OK)
                .data(Map.of("orderIds", items.stream()
                        .map(i -> i.getId())
                        .collect(Collectors.toList()),
                        "paymentMethodId", orderDTOS.get(0).getPaymentMethodId()))
                .message("Successfully create order")
                .build();
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
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orderPage = orderJPARepository.findOrderByTenant(tenantId, pageable);
        return getResult(orderPage);
    }

    @Override
    @Transactional
    public MessageResponse handleOrderCancellation(Map<String, Object> request) {
        Order order = orderJPARepository.findById((Integer) request.get("orderId"))
                .orElseThrow(() -> new RuntimeException("Not found order"));


        if (order.getStatus() != StatusOrder.CANCELLATION_REQUEST)
            throw new RuntimeException("Something wrong");

        for (var item : order.getOrderDetails()) {
            SKU sku = item.getSku();
            if (!sku.increaseStock(item.getQuantity())) throw new RuntimeException("Something wrong");
        }
        OrderCancellation orderCancellation;
        String message = "";
        if (request.get("operation").toString().equals("ACCEPT")) {
            order.setStatus(StatusOrder.CANCELED);
//        OrderCancellation orderCancellation = new OrderCancellation(order,LocalDateTime.now()
//                ,(String) request.get("reason"),"ACCEPT") ;
            orderCancellation = OrderCancellation.builder()
                    .canceledAt(LocalDateTime.now())
                    .operation("ACCEPT")
                    .order(order)
                    .reason((String) request.get("reason"))
                    .build();
            message = "Successfully cancel order";
        } else {
            orderCancellation = OrderCancellation.builder()
                    .canceledAt(LocalDateTime.now())
                    .operation("REJECT")
                    .order(order)
                    .reason((String) request.get("reason"))
                    .build();
            message = "Tenant rejected request";
        }
        orderCancellationRepository.save(orderCancellation);
        return MessageResponse.builder()
                .message(message)
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    public boolean userOwnEntity(Integer integer, String username) {
        Order order = orderJPARepository.findById(integer)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found order"));

        return true;
    }
}
