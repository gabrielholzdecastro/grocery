package com.qikserve.grocery.controller;

import com.qikserve.grocery.model.Cart;
import com.qikserve.grocery.model.ProductDetail;
import com.qikserve.grocery.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CartControllerTest {

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartController cartController;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();
    }

    @Test
    void testAddProductToCart() throws Exception {
        Cart mockCart = new Cart(
                List.of(new ProductDetail("123", "Test Product", 1000, List.of())),
                1000, 1000, 0
        );

        when(cartService.add("123")).thenReturn(mockCart);

        String responseContent = mockMvc.perform(post("/cart/add/123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(responseContent).contains("123", "Test Product", "1000", "0");

        verify(cartService, times(1)).add("123");
    }

    @Test
    void testClearCart() throws Exception {
        mockMvc.perform(delete("/cart/clear")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(cartService, times(1)).clear();
    }

    @Test
    void testGetAllCartItems() throws Exception {
        Cart mockCart = new Cart(
                Arrays.asList(
                        new ProductDetail("123", "Test Product 1", 1000, List.of()),
                        new ProductDetail("456", "Test Product 2", 2000, List.of())
                ), 3000, 3000, 0
        );

        when(cartService.getCart()).thenReturn(mockCart);

        String responseContent = mockMvc.perform(get("/cart/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(responseContent).contains("123", "Test Product 1", "456", "Test Product 2", "3000", "0");

        verify(cartService, times(1)).getCart();
    }
}
