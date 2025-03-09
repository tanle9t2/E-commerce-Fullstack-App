package com.tanle.e_commerce.event;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tanle.e_commerce.dto.OrderDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderEvent implements Event {
    private UUID eventId = UUID.randomUUID();
    private Date eventDate = new Date();
    @JsonSerialize
    private OrderDTO orderRequestDto;
    @JsonSerialize
    private OrderStatus orderStatus;

    @Override
    public UUID getEventId() {
        return eventId;
    }

    @Override
    public Date getDate() {
        return eventDate;
    }

}
