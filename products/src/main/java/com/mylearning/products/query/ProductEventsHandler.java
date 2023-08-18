package com.mylearning.products.query;

import com.mylearning.core.events.ProductReservationCancelEvent;
import com.mylearning.core.events.ProductReservedEvent;
import com.mylearning.products.core.data.ProductEntity;
import com.mylearning.products.core.data.ProductRepository;
import com.mylearning.products.core.events.ProductCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ProcessingGroup("product-group")
public class ProductEventsHandler {

    private final ProductRepository productRepository;

    public ProductEventsHandler(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @EventHandler
    public void on(ProductCreatedEvent event){
        ProductEntity productEntity = new ProductEntity();
        BeanUtils.copyProperties(event, productEntity);
        productRepository.save(productEntity);
    }

    @EventHandler
    public void on (ProductReservedEvent productReservedEvent){
        ProductEntity productEntity = productRepository.findByProductId(productReservedEvent.getProductId());
        productEntity.setQuantity(productReservedEvent.getQuantity());
        productRepository.save(productEntity);
        log.info("ProductReserveEvent is called for productId:%s and orderId:%s"
                , productReservedEvent.getProductId()
                , productReservedEvent.getOrderId());

    }

    @EventHandler
    public void on(ProductReservationCancelEvent productReservationCancelEvent)
    {
        ProductEntity currentStoredProduct = productRepository.findByProductId(productReservationCancelEvent.getProductId());
        int updatedQuantity = currentStoredProduct.getQuantity()+ productReservationCancelEvent.getQuantity();
        currentStoredProduct.setQuantity(updatedQuantity);
        productRepository.save(currentStoredProduct);

    }
}
