package com.mylearning.core.commands;

import lombok.Builder;
import lombok.Data;
import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@Value
@Builder
public class CancelProductReservationCommand {
    @TargetAggregateIdentifier
    private final String productId;
    private final String orderId;
    private final int quantity;
    private final String userId;
    private final String reason;
}
