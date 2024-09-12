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

import java.util.List;
import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

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
    void shouldReturnAllProducts() {
        List<Product> mockProducts = Arrays.asList(
                new Product("1", "Product 1", 1000),
                new Product("2", "Product 2", 2000)
        );
        when(productClient.getAll()).thenReturn(mockProducts);

        List<Product> products = productService.getAll();

        assertThat(products).hasSize(2);
        assertThat(products.get(0).getName()).isEqualTo("Product 1");
        assertThat(products.get(1).getPrice()).isEqualTo(2000);

        verify(productClient, times(1)).getAll();
    }

    @Test
    void shouldReturnProductById() {
        Promotion mockPromotion = new Promotion("promo1", PromotionType.FLAT_PERCENT, 0, 0, 0, 10);
        ProductDetail mockProductDetail = new ProductDetail("1", "Product 1", 1000, List.of(mockPromotion));

        when(productClient.getProductDetail("1")).thenReturn(mockProductDetail);

        ProductDetail productDetail = productService.getById("1");

        assertThat(productDetail).isNotNull();
        assertThat(productDetail.getName()).isEqualTo("Product 1");
        assertThat(productDetail.getPrice()).isEqualTo(1000);
        assertThat(productDetail.getPromotions()).hasSize(1);
        assertThat(productDetail.getPromotions().getFirst().getType()).isEqualTo(PromotionType.FLAT_PERCENT);

        verify(productClient, times(1)).getProductDetail("1");
    }
}
