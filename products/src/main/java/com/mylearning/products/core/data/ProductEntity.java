package com.mylearning.products.core.data;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Data
public class ProductEntity implements Serializable   {
    private static final long serialVersionUID = 223223;

    @Id
    @Column(unique = true)
    private String productId;

    @Column(unique = true)
    private String title;
    private BigDecimal price;
    private Integer quantity;
}
