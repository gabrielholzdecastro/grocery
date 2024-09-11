package com.qikserve.grocery.controller;

import com.qikserve.grocery.model.Cart;
import com.qikserve.grocery.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add/{idProduct}")
    public ResponseEntity<Cart> add(@PathVariable String idProduct) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.add(idProduct));
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clear() {
        cartService.clear();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}
