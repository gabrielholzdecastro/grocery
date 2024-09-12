package com.qikserve.grocery.controller;

import com.qikserve.grocery.model.Product;
import com.qikserve.grocery.model.ProductDetail;
import com.qikserve.grocery.service.ProductService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    void testGetAllProducts() throws Exception {
        List<Product> mockProducts = Arrays.asList(
                new Product("1", "Product 1", 1000),
                new Product("2", "Product 2", 2000)
        );

        when(productService.getAll()).thenReturn(mockProducts);

        String responseContent = mockMvc.perform(get("/product/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(responseContent).contains("Product 1", "Product 2", "1000", "2000");

        verify(productService, times(1)).getAll();
    }

    @Test
    void testGetProductByIdFound() throws Exception {
        ProductDetail mockProductDetail = new ProductDetail("1", "Product 1", 1000, List.of());

        when(productService.getById("1")).thenReturn(mockProductDetail);

        String responseContent = mockMvc.perform(get("/product/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(responseContent).contains("1", "Product 1", "1000");

        verify(productService, times(1)).getById("1");
    }

    @Test
    void testGetProductByIdNotFound() throws Exception {
        when(productService.getById("999")).thenReturn(null);

        mockMvc.perform(get("/product/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(productService, times(1)).getById("999");
    }
}
