package com.mylearning.products.core.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "productlookup")
public class ProductLookupEntity implements Serializable {
    private static final Long serialVersionUID = 42L;

    @Id
    private String productId;

    @Column(unique = true)
    private String title;

}
