package com.mylearning.products.query;

import com.mylearning.products.core.data.ProductEntity;
import com.mylearning.products.core.data.ProductRepository;
import com.mylearning.products.query.rest.ProductRestModel;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductsQueryHandler {
    private final ProductRepository productRepository;

    public ProductsQueryHandler(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @QueryHandler
    public List<ProductRestModel> findProducts(FindProductsQuery productsQuery){
        //List<ProductRestModel> productsRest = new ArrayList<>();
        List<ProductEntity> storedProducts = productRepository.findAll();
        List<ProductRestModel> productsRest = storedProducts.stream().map(
                productEntity -> {
                    ProductRestModel productRestModel = new ProductRestModel();
                    BeanUtils.copyProperties(productEntity, productRestModel);
                    return productRestModel;
                }
        ).collect(Collectors.toList());
        return productsRest;
    }
}
