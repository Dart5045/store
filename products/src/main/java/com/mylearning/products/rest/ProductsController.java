package com.mylearning.products.rest;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")

public class ProductsController {

    @PostMapping
    public String createProduct(){
        return "Post";
    }

    @GetMapping
    public String getProduct(){
        return "Get";
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
