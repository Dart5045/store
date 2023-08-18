package com.mylearning.products.command;

import com.mylearning.core.commands.CancelProductReservationCommand;
import com.mylearning.core.commands.ReserveProductCommand;
import com.mylearning.core.events.ProductReservationCancelEvent;
import com.mylearning.core.events.ProductReservedEvent;
import com.mylearning.products.core.events.ProductCreatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

@Aggregate
public class ProductAggregate {
    @AggregateIdentifier
    private String productId;
    private String title;
    private BigDecimal price;
    private Integer quantity;

    public ProductAggregate() {
    }

    @CommandHandler
    public ProductAggregate(CreateProductCommand createProductCommand ) {
        if(createProductCommand.getPrice().compareTo(BigDecimal.ZERO)<=0){
          throw new IllegalArgumentException("Price cannot be less or equals than zero");
        }
        if(createProductCommand.getTitle() == null
                ||createProductCommand.getTitle().isBlank()){
            throw new IllegalArgumentException("Title cannot be empty");
        }
        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent();
        BeanUtils.copyProperties(createProductCommand,productCreatedEvent);

        AggregateLifecycle.apply(productCreatedEvent);
    }


    @CommandHandler
    public void handle(ReserveProductCommand reserveProductCommand){
        if(quantity< reserveProductCommand.getQuantity()){
            throw new IllegalArgumentException("Insufficient number of items in stock");
        }
        ProductReservedEvent event = ProductReservedEvent
                .builder()
                .orderId(reserveProductCommand.getOrderId())
                .userId(reserveProductCommand.getUserId())
                .productId(reserveProductCommand.getProductId())
                .quantity(reserveProductCommand.getQuantity())
                .build();
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(ProductCreatedEvent productCreatedEvent){
        this.productId = productCreatedEvent.getProductId();
        this.price = productCreatedEvent.getPrice();
        this.quantity= productCreatedEvent.getQuantity();
        this.title = productCreatedEvent.getTitle();
    }

    @EventSourcingHandler
    public void on(ProductReservedEvent productReservedEvent){

        this.quantity -= productReservedEvent.getQuantity();
    }

    @CommandHandler(commandName = "productId")
    public void handle(CancelProductReservationCommand cancelProductReservationCommand)
    {
        ProductReservationCancelEvent productReservationCancelEvent = ProductReservationCancelEvent
                .builder()
                .orderId(cancelProductReservationCommand.getOrderId())
                .productId(cancelProductReservationCommand.getProductId())
                .quantity(cancelProductReservationCommand.getQuantity())
                .userId(cancelProductReservationCommand.getUserId())
                .build();
        AggregateLifecycle.apply(productReservationCancelEvent);
    }

    @EventSourcingHandler
    public void on(ProductReservationCancelEvent productReservationCancelEvent){
        this.quantity += productReservationCancelEvent.getQuantity();
    }

}
