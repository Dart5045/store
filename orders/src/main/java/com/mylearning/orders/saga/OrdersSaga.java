package com.mylearning.orders.saga;

import com.mylearning.core.commands.CancelProductReservationCommand;
import com.mylearning.core.commands.ProcessPaymentCommand;
import com.mylearning.core.events.PaymentProcessedEvent;
import com.mylearning.core.events.ProductReservationCancelEvent;
import com.mylearning.core.model.User;
import com.mylearning.core.commands.ReserveProductCommand;
import com.mylearning.core.events.ProductReservedEvent;
import com.mylearning.core.query.FetchUserPaymentDetailsQuery;
import com.mylearning.orders.command.commands.RejectOrderCommand;
import com.mylearning.orders.core.events.OrderCreatedEvent;
import com.mylearning.orders.core.events.OrderRejectedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.CommandResultMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;

import javax.annotation.Nonnull;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Saga
public class OrdersSaga {
    private transient CommandGateway commandGateway;
    private transient QueryGateway queryGateway;

    public OrdersSaga(CommandGateway commandGateway, QueryGateway queryGateway) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
    }

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderCreatedEvent orderCreatedEvent)
    {
        ReserveProductCommand reserveProductCommand = ReserveProductCommand
                .builder()
                .orderId(orderCreatedEvent.getOrderId())
                .productId(orderCreatedEvent.getProductId())
                .userId(orderCreatedEvent.getUserId())
                .quantity(orderCreatedEvent.getQuantity())
                .build();
        log.info("Order created event handle for orderId %s",orderCreatedEvent.getOrderId());
        commandGateway.send(reserveProductCommand
                ,new CommandCallback<ReserveProductCommand, Object>() {
                    @Override
                    public void onResult(
                              @Nonnull CommandMessage<? extends ReserveProductCommand> commandMessage
                            , @Nonnull CommandResultMessage<?  extends Object> commandResultMessage) {

                    }
                });
    }
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(ProductReservedEvent productReservedEvent)
    {
        log.info("ProductReserveEvent is called for productId %s", productReservedEvent.getProductId());
        FetchUserPaymentDetailsQuery fetchUserPaymentDetailsQuery =
                new FetchUserPaymentDetailsQuery(productReservedEvent.getUserId());
        User userPaymentDetail = null;
        try {
            userPaymentDetail = queryGateway.query(fetchUserPaymentDetailsQuery, ResponseTypes.instanceOf(User.class)).join();
        }catch (Exception ex){
            log.error(ex.getMessage());
            cancelProductReservation(productReservedEvent,ex.getMessage());
            return;
        }
        if(userPaymentDetail== null){
            cancelProductReservation(productReservedEvent,"Could not fetch user payment detail");
        }
        log.info("Successfully fetched user payment details for user %s");
        ProcessPaymentCommand processPaymentCommand =  ProcessPaymentCommand
                .builder()
                .orderId(productReservedEvent.getOrderId())
                .paymentDetails(userPaymentDetail.getPaymentDetails())
                .paymentId(UUID.randomUUID().toString())
                .build();
        String result = null;
        try {
            result = commandGateway.sendAndWait(processPaymentCommand, 10, TimeUnit.SECONDS);
        }catch (Exception ex){
            log.error(ex.getMessage());
            cancelProductReservation(productReservedEvent,ex.getMessage());
            return;

        }
        if(result == null){
            log.info("The processPaymentCommand resulted is null. Initiating a compensation transaction ");
            cancelProductReservation(productReservedEvent,"Could not process user payment with provided payment details");


        }
    }

    private void cancelProductReservation(ProductReservedEvent productReservedEvent
            ,String reason){
        CancelProductReservationCommand cancelProductReservationCommand= CancelProductReservationCommand
                .builder()
                .orderId(productReservedEvent.getOrderId())
                .userId(productReservedEvent.getOrderId())
                .quantity(productReservedEvent.getQuantity())
                .reason(reason)
                .build();

        commandGateway.send(cancelProductReservationCommand);
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handler(PaymentProcessedEvent paymentProcessedEvent){

    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handler(ProductReservationCancelEvent productReservationCancelEvent){
        RejectOrderCommand rejectOrderCommand = new RejectOrderCommand(
                productReservationCancelEvent.getOrderId(),
                productReservationCancelEvent.getReason()
        );
        commandGateway.send(rejectOrderCommand);

    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderIs")
    public void handler(OrderRejectedEvent orderRejectedEvent){
        log.info("Successfully rejected order with id = "+orderRejectedEvent.getOrderId());

    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handler(){
    }
}
