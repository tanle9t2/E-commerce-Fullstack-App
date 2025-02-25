package com.tanle.e_commerce.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tanle.e_commerce.entities.CompositeKey.OrderDetailKey;
import com.tanle.e_commerce.entities.enums.StatusOrder;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "orders")
//@Inheritance(strategy = InheritanceType.JOINED)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private int id;

    @Column(name = "create_at")
    @UpdateTimestamp
    private LocalDateTime createdAt;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private StatusOrder status;
    @Column(name = "note")
    private String note;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private MyUser myUser;

    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToOne(mappedBy = "order")
    @PrimaryKeyJoinColumn
    @JsonIgnore
    private OrderCancellation orderCancellation;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails;
    @ManyToOne
    @JoinColumn(name = "payment_method_id")
    private PaymentMethod paymentMethod;
    public void addOrderDetail(OrderDetail orderDetail) {
        if(orderDetails == null) this.orderDetails = new ArrayList<>();
        OrderDetailKey key = new OrderDetailKey(this.getId(),orderDetail.getSku().getId(),LocalDateTime.now());
        orderDetail.setOrder(this);
        orderDetail.setId(key);
        orderDetails.add(orderDetail);
    }
    public void addOrderDetail(List<OrderDetail> orderDetailList) {
        orderDetailList.forEach(o -> this.addOrderDetail(o));
    }

}
