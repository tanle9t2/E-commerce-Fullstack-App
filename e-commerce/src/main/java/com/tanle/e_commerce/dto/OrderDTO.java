package com.tanle.e_commerce.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tanle.e_commerce.entities.Tenant;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Document(indexName = "order-index")
public class OrderDTO {
    @Id
    private int id;
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
    private LocalDateTime createdAt;
    @Field(type = FieldType.Double)
    private double totalPrice;
    @Field(type = FieldType.Integer)
    private int quantity;
    @Field(type = FieldType.Text)
    private String status;
    @Field(type = FieldType.Object)
    private UserOrder userOrder;
    @Field(type = FieldType.Integer)
    private TenantOrder tenantOrder;
    @Field(type = FieldType.Object)
    private List<OrderDetailDTO> itemList;
    @Field(type = FieldType.Object)
    private AddressDTO receiptAddress;
    @Field(type = FieldType.Integer)
    private int paymentId;
    @Field(type = FieldType.Date)
    private LocalDateTime payTime;
    @Data
    @NoArgsConstructor
    public class UserOrder {
        private int userId;
        private String fullName;

    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class TenantOrder {
        private int tenantId;
        private String tenantName;

    }
    public TenantOrder buildTenantOrder(int userId, String tenantName) {
        TenantOrder tenantOrder = new TenantOrder();
        tenantOrder.setTenantId(userId);
        tenantOrder.setTenantName(tenantName);
        return tenantOrder;
    }
    public UserOrder buildUserOrder(int userId, String fullName) {
        UserOrder userOrder = new UserOrder();
        userOrder.setUserId(userId);
        userOrder.setFullName(fullName);
        return userOrder;
    }
}
