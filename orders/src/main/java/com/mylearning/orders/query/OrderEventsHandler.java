package com.mylearning.orders.query;

import com.mylearning.orders.core.data.OrderEntity;
import com.mylearning.orders.core.data.OrdersRepository;
import com.mylearning.orders.core.events.OrderCreatedEvent;
import com.mylearning.orders.core.events.OrderRejectedEvent;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@ProcessingGroup("order-group")
public class OrderEventsHandler {
    private final OrdersRepository orderRepository;

    public OrderEventsHandler(OrdersRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventHandler
    public void on(OrderCreatedEvent event){
        OrderEntity orderEntity = new OrderEntity();
        BeanUtils.copyProperties(event, orderEntity);
        orderRepository.save(orderEntity);
    }

    @EventHandler
    public void on(OrderRejectedEvent event){
        OrderEntity orderEntity = orderRepository.findByOrderId(event.getOrderId());
        orderEntity.setOrderStatus(event.getOrderStatus());
        orderRepository.save(orderEntity);
    }
}