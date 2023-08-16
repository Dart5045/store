package com.mylearning.orders.core.data;

import com.mylearning.orders.core.model.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Table(name = "orders")
@Data
public class OrderEntity implements Serializable {
    private static final long serialVersionUID = 223224;
    @Id
    @Column(unique = true)
    public  String orderId;
    private String userId;
    private String productId;
    private int quantity;
    private String addressId;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
}
