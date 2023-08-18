package com.mylearning.orders.core.events;

import com.mylearning.orders.core.model.OrderStatus;
import lombok.Builder;
import lombok.Value;

@Value
public class OrderRejectedEvent {
    private final String orderId;
    private final String reason;
    private final OrderStatus orderStatus = OrderStatus.REJECTED;
}