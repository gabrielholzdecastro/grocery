package com.qikserve.grocery.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Product {
    private String id;
    private String name;
    private int price;

    public Product() {}

    public Product(String id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

}
