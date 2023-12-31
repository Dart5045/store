package com.mylearning.products.core.data;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductLookupRepository extends JpaRepository<ProductLookupEntity,String> {

    ProductLookupEntity findByProductIdOrTitle(String id, String title);

}
