package com.mylearning.products.core.data;

import com.mylearning.products.core.events.ProductCreatedEvent;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("product-group")
public class ProductLookupEventsHandler {

    private final ProductLookupRepository productLookupRepository;

    public ProductLookupEventsHandler(ProductLookupRepository productLookupRepository) {
        this.productLookupRepository = productLookupRepository;
    }

    @EventHandler
    public void on(ProductCreatedEvent event){
        ProductLookupEntity productLookupEntity = new ProductLookupEntity(
                event.getProductId(),
                event.getTitle()
        );
        productLookupRepository.save(productLookupEntity);
    }

}
