package com.qikserve.grocery.client;

import com.qikserve.grocery.model.Product;
import com.qikserve.grocery.model.ProductDetail;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "productClient", url = "http://localhost:8081")
public interface ProductClient {

    @GetMapping("/products")
    List<Product> getAll();

    @GetMapping("/products/{id}")
    ProductDetail getProductDetail(@PathVariable String id);
}