package com.qikserve.grocery.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    private List<ProductDetail> products;
    private int finalPrice;
    private int totalPrice;
    private int discount;
}
