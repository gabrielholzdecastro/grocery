package com.qikserve.grocery.service;

import com.qikserve.grocery.client.ProductClient;
import com.qikserve.grocery.model.Product;
import com.qikserve.grocery.model.ProductDetail;
import com.qikserve.grocery.model.Promotion;
import com.qikserve.grocery.model.PromotionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Arrays;

class ProductServiceTest {

    @Mock
    private ProductClient productClient;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllProducts() {
        // Mock da resposta do Feign Client
        List<Product> mockProducts = Arrays.asList(
                new Product("1", "Product 1", 1000),
                new Product("2", "Product 2", 2000)
        );
        when(productClient.getAll()).thenReturn(mockProducts);

        // Chama o método
        List<Product> products = productService.getAll();

        // Verifica se o resultado está correto
        assertNotNull(products);
        assertEquals(2, products.size());
        assertEquals("Product 1", products.getFirst().getName());
    }

    @Test
    void testGetProductById() {
        // Mock de um produto detalhado com promoção
        Promotion mockPromotion = new Promotion("promo1", PromotionType.FLAT_PERCENT, 0, 0, 0, 10);
        ProductDetail mockProductDetail = new ProductDetail("1", "Product 1", 1000, List.of(mockPromotion));

        when(productClient.getProductDetail("1")).thenReturn(mockProductDetail);

        // Chama o método
        ProductDetail productDetail = productService.getById("1");

        // Verifica se o resultado está correto
        assertNotNull(productDetail);
        assertEquals("Product 1", productDetail.getName());
        assertEquals(1000, productDetail.getPrice());
        assertEquals(1, productDetail.getPromotions().size());
        assertEquals(PromotionType.FLAT_PERCENT, productDetail.getPromotions().getFirst().getType());
    }
}