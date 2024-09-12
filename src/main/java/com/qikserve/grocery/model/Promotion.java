package com.qikserve.grocery.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Promotion {

    private String id;
    private PromotionType type;
    @JsonProperty("required_qty")
    private int requiredQty;
    private int price;
    @JsonProperty("free_qty")
    private int freeQty;
    private int amount;

}
