package com.mylearning.products.command.interceptors;

import com.mylearning.products.command.CreateProductCommand;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.List;
import java.util.function.BiFunction;

@Component
@Slf4j
public class CreateProductCommandInterceptors implements MessageDispatchInterceptor<CommandMessage<?>> {
    @Nonnull
    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(@Nonnull List<? extends CommandMessage<?>> list) {
        return (integer, commandMessage) -> {
            log.info("Intercepted command "+commandMessage.getPayloadType());
            if(CreateProductCommand.class.equals(commandMessage.getPayloadType())){

                CreateProductCommand createProductCommand = (CreateProductCommand) commandMessage.getPayload();
                if(createProductCommand.getPrice().compareTo(BigDecimal.ZERO)<=0){
                    throw new IllegalArgumentException("Price cannot be less or equals than zero");
                }
                if(createProductCommand.getTitle() == null
                        ||createProductCommand.getTitle().isBlank()){
                    throw new IllegalArgumentException("Title cannot be empty");
                }
            }
            return commandMessage;
        };
    }
}
