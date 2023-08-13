package com.mylearning.products.query.rest;

import com.mylearning.products.query.FindProductsQuery;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/products/query")
public class ProductsQueryController {

    private  final QueryGateway queryGateway;

    public ProductsQueryController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @GetMapping
    public List<ProductRestModel> getProducts(){
        FindProductsQuery productsQuery = new FindProductsQuery();
        List<ProductRestModel> products = queryGateway.query(productsQuery,
                ResponseTypes.multipleInstancesOf(ProductRestModel.class)).join();
        return products;
    }
}
