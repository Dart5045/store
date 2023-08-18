package com.mylearning.user.query;

import com.mylearning.core.model.PaymentDetails;
import com.mylearning.core.model.User;
import com.mylearning.core.query.FetchUserPaymentDetailsQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
public class UserEventHandler {


    @QueryHandler
    public User on(FetchUserPaymentDetailsQuery query){
        PaymentDetails paymentDetails = PaymentDetails.builder()
                .cardNumber("123Card")
                .cvv("123")
                .name("SERGEY KARGOPOLOV")
                .validUntilMonth(12)
                .validUntilYear(2030)
                .build();

        User userRest = User.builder()
                .firstName("Sergey")
                .lastName("Kargopolov")
                .userId(query.getUserId())
                .paymentDetails(paymentDetails)
                .build();
        return userRest;
    }

}
