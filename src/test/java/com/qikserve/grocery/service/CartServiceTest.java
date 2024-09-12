package com.qikserve.grocery.service;

import com.qikserve.grocery.model.Cart;
import com.qikserve.grocery.model.ProductDetail;
import com.qikserve.grocery.model.Promotion;
import com.qikserve.grocery.model.PromotionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

class CartServiceTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private CartService cartService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        cartService.clear();
    }

    @Test
    void testAddProductWithoutPromotion() {
        ProductDetail productDetail = new ProductDetail("123", "Simple Product", 500, Collections.emptyList());

        when(productService.getById("123")).thenReturn(productDetail);

        Cart cart = cartService.add("123");

        assertThat(cart.getProducts()).hasSize(1);
        assertThat(cart.getTotalPrice()).isEqualTo(500);
        assertThat(cart.getFinalPrice()).isEqualTo(500);
        assertThat(cart.getDiscount()).isZero();

        verify(productService, times(1)).getById("123");
    }

    @Test
    void testApplyQtyBasedPriceOverride() {
        Promotion promotion = new Promotion("promo1", PromotionType.QTY_BASED_PRICE_OVERRIDE, 2, 900, 0, 0);
        ProductDetail productDetail = new ProductDetail("124", "Promo Product", 500, List.of(promotion));

        when(productService.getById("124")).thenReturn(productDetail);

        cartService.add("124");
        Cart cart = cartService.add("124");

        assertThat(cart.getProducts()).hasSize(2);
        assertThat(cart.getTotalPrice()).isEqualTo(1000);
        assertThat(cart.getFinalPrice()).isEqualTo(900);
        assertThat(cart.getDiscount()).isEqualTo(100);

        verify(productService, times(2)).getById("124");
    }

    @Test
    void testApplyBuyXGetYFree() {
        Promotion promotion = new Promotion("promo2", PromotionType.BUY_X_GET_Y_FREE, 2, 0, 1, 0);
        ProductDetail productDetail = new ProductDetail("125", "Free Product", 300, List.of(promotion));

        when(productService.getById("125")).thenReturn(productDetail);

        cartService.add("125");
        cartService.add("125");
        Cart cart = cartService.add("125");

        assertThat(cart.getProducts()).hasSize(3);
        assertThat(cart.getTotalPrice()).isEqualTo(900);
        assertThat(cart.getFinalPrice()).isEqualTo(600);
        assertThat(cart.getDiscount()).isEqualTo(300);

        verify(productService, times(3)).getById("125");
    }

    @Test
    void testApplyFlatPercentPromotion() {
        Promotion promotion = new Promotion("promo3", PromotionType.FLAT_PERCENT, 0, 0, 0, 10);
        ProductDetail productDetail = new ProductDetail("126", "Percent Product", 1000, List.of(promotion));

        when(productService.getById("126")).thenReturn(productDetail);

        Cart cart = cartService.add("126");

        assertThat(cart.getProducts()).hasSize(1);
        assertThat(cart.getTotalPrice()).isEqualTo(1000);
        assertThat(cart.getFinalPrice()).isEqualTo(900);
        assertThat(cart.getDiscount()).isEqualTo(100);

        verify(productService, times(1)).getById("126");
    }

    @Test
    void testClearCart() {
        ProductDetail productDetail = new ProductDetail("127", "Product", 500, Collections.emptyList());

        when(productService.getById("127")).thenReturn(productDetail);

        cartService.add("127");
        cartService.clear();

        Cart cart = cartService.add("127");

        assertThat(cart.getProducts()).hasSize(1);
        assertThat(cart.getTotalPrice()).isEqualTo(500);
        assertThat(cart.getFinalPrice()).isEqualTo(500);
        assertThat(cart.getDiscount()).isZero();

        verify(productService, times(2)).getById("127");
    }

    @Test
    void testClearCartAfterAddingMultipleProducts() {
        ProductDetail product1 = new ProductDetail("001", "Product 1", 300, Collections.emptyList());
        ProductDetail product2 = new ProductDetail("002", "Product 2", 500, Collections.emptyList());

        when(productService.getById("001")).thenReturn(product1);
        when(productService.getById("002")).thenReturn(product2);

        cartService.add("001");
        cartService.add("002");

        Cart cart = cartService.getCart();
        assertThat(cart.getProducts()).hasSize(2);
        assertThat(cart.getTotalPrice()).isEqualTo(800);
        assertThat(cart.getFinalPrice()).isEqualTo(800);

        cartService.clear();

        assertThat(cart.getProducts()).isEmpty();
        assertThat(cart.getTotalPrice()).isZero();
        assertThat(cart.getFinalPrice()).isZero();
        assertThat(cart.getDiscount()).isZero();

        verify(productService, times(1)).getById("001");
        verify(productService, times(1)).getById("002");
    }
}
