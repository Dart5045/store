package com.mylearning.products.rest;

import com.mylearning.products.rest.command.CreateProductCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/products")

public class ProductsController {

    private final Environment env;
    private final CommandGateway commandGateway;

    public ProductsController(Environment env, CommandGateway commandGateway) {
        this.env = env;
        this.commandGateway = commandGateway;
    }

    @PostMapping
    public String createProduct(@RequestBody CreateProductRestModel createProductRestModel){

        CreateProductCommand productCommand = CreateProductCommand
                .builder()
                .price(createProductRestModel.getPrice())
                .quantity(createProductRestModel.getQuantity())
                .title(createProductRestModel.getTitle())
                .productId(UUID.randomUUID().toString())
                .build();
        String returnValue;
        try{
            returnValue =  commandGateway.sendAndWait(productCommand);
        }catch (Exception ex){
            System.out.println("Error");
            returnValue = ex.getLocalizedMessage();
        }
        return returnValue;
    }

    @GetMapping
    public String getProduct(){
        return "Get"+this.env.getProperty("local.server.port");
    }

    @PutMapping
    public String updateProduct(){
        return "Put";
    }

    @DeleteMapping
    public String deleteProduct(){
        return "Delete";
    }
}
