package com.qikserve.grocery.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ProductDetail extends Product {

    private List<Promotion> promotions;

    public ProductDetail() {
        super();
    }

    public ProductDetail(String id, String name, int price, List<Promotion> promotions) {
        super(id, name, price);
        this.promotions = promotions;
    }

}

