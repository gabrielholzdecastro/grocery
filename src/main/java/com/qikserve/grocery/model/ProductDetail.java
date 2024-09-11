package com.qikserve.grocery.model;

import lombok.Data;

import java.util.List;

@Data
public class ProductDetail extends Product {

    private List<Promotion> promotions;

}
