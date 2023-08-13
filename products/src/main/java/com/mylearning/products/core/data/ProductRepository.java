package com.mylearning.products.core.data;

import com.mylearning.products.core.data.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, String> {
    ProductEntity findByProductId(String productID);
    ProductEntity findByProductIdOrTitle(String productID, String titles);

}
