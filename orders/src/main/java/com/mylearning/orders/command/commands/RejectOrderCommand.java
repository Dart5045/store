package com.mylearning.orders.command.commands;

import com.mylearning.orders.core.model.OrderStatus;
import lombok.Builder;
import lombok.Data;
import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Value
public class RejectOrderCommand {
    @TargetAggregateIdentifier
    public final String orderId;
    private final String reason;
}
