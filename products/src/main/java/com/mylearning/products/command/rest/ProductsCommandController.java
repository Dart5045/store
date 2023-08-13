package com.mylearning.products.command.rest;

import com.mylearning.products.command.CreateProductCommand;
import jakarta.validation.Valid;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/products")

public class ProductsCommandController {

    private final Environment env;
    private final CommandGateway commandGateway;

    public ProductsCommandController(Environment env, CommandGateway commandGateway) {
        this.env = env;
        this.commandGateway = commandGateway;
    }

    @PostMapping
    public String createProduct(@Valid @RequestBody CreateProductRestModel createProductRestModel){

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
