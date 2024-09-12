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
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.List;

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
        ProductDetail productDetail = new ProductDetail();
        productDetail.setId("123");
        productDetail.setName("Simple Product");
        productDetail.setPrice(500);
        productDetail.setPromotions(Collections.emptyList());

        when(productService.getById("123")).thenReturn(productDetail);

        Cart cart = cartService.add("123");

        assertEquals(1, cart.getProducts().size());
        assertEquals(500, cart.getTotalPrice());
        assertEquals(500, cart.getFinalPrice());
        assertEquals(0, cart.getDiscount());
    }

    @Test
    void testApplyQtyBasedPriceOverride() {
        Promotion promotion = new Promotion("promo1", PromotionType.QTY_BASED_PRICE_OVERRIDE, 2, 900, 0, 0);
        ProductDetail productDetail = new ProductDetail();
        productDetail.setId("124");
        productDetail.setName("Promo Product");
        productDetail.setPrice(500);
        productDetail.setPromotions(List.of(promotion));

        when(productService.getById("124")).thenReturn(productDetail);

        cartService.add("124");
        Cart cart = cartService.add("124");

        assertEquals(2, cart.getProducts().size());
        assertEquals(1000, cart.getTotalPrice());
        assertEquals(900, cart.getFinalPrice());
        assertEquals(100, cart.getDiscount());
    }

    @Test
    void testApplyBuyXGetYFree() {
        Promotion promotion = new Promotion("promo2", PromotionType.BUY_X_GET_Y_FREE, 2, 0, 1, 0);
        ProductDetail productDetail = new ProductDetail();
        productDetail.setId("125");
        productDetail.setName("Free Product");
        productDetail.setPrice(300);
        productDetail.setPromotions(List.of(promotion));

        when(productService.getById("125")).thenReturn(productDetail);

        cartService.add("125");
        cartService.add("125");
        Cart cart = cartService.add("125");

        assertEquals(3, cart.getProducts().size());
        assertEquals(900, cart.getTotalPrice());
        assertEquals(600, cart.getFinalPrice());
        assertEquals(300, cart.getDiscount());
    }

    @Test
    void testApplyFlatPercentPromotion() {
        Promotion promotion = new Promotion("promo3", PromotionType.FLAT_PERCENT, 0, 0, 0, 10);
        ProductDetail productDetail = new ProductDetail();
        productDetail.setId("126");
        productDetail.setName("Percent Product");
        productDetail.setPrice(1000);
        productDetail.setPromotions(List.of(promotion));

        when(productService.getById("126")).thenReturn(productDetail);

        Cart cart = cartService.add("126");

        assertEquals(1, cart.getProducts().size());
        assertEquals(1000, cart.getTotalPrice());
        assertEquals(900, cart.getFinalPrice());
        assertEquals(100, cart.getDiscount());
    }

    @Test
    void testClearCart() {
        ProductDetail productDetail = new ProductDetail();
        productDetail.setId("127");
        productDetail.setName("Product");
        productDetail.setPrice(500);
        productDetail.setPromotions(Collections.emptyList());

        when(productService.getById("127")).thenReturn(productDetail);

        cartService.add("127");
        cartService.clear();

        Cart cart = cartService.add("127");

        assertEquals(1, cart.getProducts().size());
        assertEquals(500, cart.getTotalPrice());
        assertEquals(500, cart.getFinalPrice());
        assertEquals(0, cart.getDiscount());
    }
}
