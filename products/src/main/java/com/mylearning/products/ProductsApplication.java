package com.mylearning.products;

import com.mylearning.products.command.interceptors.CreateProductCommandInterceptors;
import org.axonframework.commandhandling.CommandBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ProductsApplication
{
    public static void main(String[] args) {
        SpringApplication.run(ProductsApplication.class,args);
    }

    @Autowired
    public void registerProductCommandInterceptor(ApplicationContext applicationContext
            , CommandBus commandBus){

        commandBus.registerDispatchInterceptor(applicationContext.getBean(CreateProductCommandInterceptors.class));
    }
}
