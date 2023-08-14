package com.mylearning.products.command.interceptors;

import com.mylearning.products.command.CreateProductCommand;
import com.mylearning.products.core.data.ProductLookupEntity;
import com.mylearning.products.core.data.ProductLookupRepository;
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

    private final ProductLookupRepository productLookupRepository;

    public CreateProductCommandInterceptors(ProductLookupRepository productLookupRepository) {
        this.productLookupRepository = productLookupRepository;
    }

    @Nonnull
    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(@Nonnull List<? extends CommandMessage<?>> list) {
        return (integer, commandMessage) -> {
            log.info("Intercepted command "+commandMessage.getPayloadType());
            if(CreateProductCommand.class.equals(commandMessage.getPayloadType())){
                CreateProductCommand createProductCommand = (CreateProductCommand) commandMessage.getPayload();
                ProductLookupEntity byProductIdOrTitle = productLookupRepository
                        .findByProductIdOrTitle(createProductCommand.getProductId()
                                , createProductCommand.getTitle());
                if(byProductIdOrTitle != null)
                {
                    throw new IllegalStateException(
                            String.format(
                                    "Product with id %s or title %s already exist"
                                    ,createProductCommand.getProductId()
                                    ,createProductCommand.getTitle()
                            )
                    );
                }
            }
            return commandMessage;
        };
    }
}
