package com.qikserve.grocery.service;

import com.qikserve.grocery.client.ProductClient;
import com.qikserve.grocery.model.Product;
import com.qikserve.grocery.model.ProductDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductClient productClient;

    public List<Product> getAll() {
        return productClient.getAll();
    }

    public ProductDetail getById(String id) {
        return productClient.getProductDetail(id);
    }

}
