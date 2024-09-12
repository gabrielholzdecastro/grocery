package com.qikserve.grocery.service;

import com.qikserve.grocery.model.Cart;
import com.qikserve.grocery.model.ProductDetail;
import com.qikserve.grocery.model.Promotion;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CartService {

    @Getter
    private final Cart cart = new Cart(new ArrayList<>(), 0, 0, 0);

    @Autowired
    private ProductService productService;

    public void clear() {
        cart.getProducts().clear();
        cart.setTotalPrice(0);
        cart.setFinalPrice(0);
        cart.setDiscount(0);
    }

    public Cart add(String idProduct) {
        ProductDetail productDetail = productService.getById(idProduct);
        cart.getProducts().add(productDetail);
        applyPromotion(productDetail);
        return cart;
    }

    private void applyPromotion(ProductDetail productDetail) {
        if (productDetail.getPromotions().isEmpty()) {
            cart.setTotalPrice(cart.getTotalPrice() + productDetail.getPrice());
            cart.setFinalPrice(cart.getFinalPrice() + productDetail.getPrice());
            cart.setDiscount(cart.getTotalPrice() - cart.getFinalPrice());
            return;
        }

        final Promotion promotion = productDetail.getPromotions().getFirst();
        long count = cart.getProducts().stream()
                .filter(p -> p.getId().equals(productDetail.getId()))
                .count();

        switch (promotion.getType()) {
            case QTY_BASED_PRICE_OVERRIDE:
                handleQtyBasedPriceOverride(productDetail, promotion, count);
                break;
            case BUY_X_GET_Y_FREE:
                handleBuyXGetYFree(productDetail, promotion, count);
                break;
            case FLAT_PERCENT:
                handleFlatPercent(productDetail, promotion);
                break;
        }

        cart.setDiscount(cart.getTotalPrice() - cart.getFinalPrice());
    }

    private void handleQtyBasedPriceOverride(ProductDetail productDetail, Promotion promotion, long count) {
        cart.setTotalPrice(cart.getTotalPrice() + productDetail.getPrice());
        if (count % promotion.getRequiredQty() == 0) {
            cart.setFinalPrice(cart.getFinalPrice() - productDetail.getPrice());
            cart.setFinalPrice(cart.getFinalPrice() + promotion.getPrice());
        } else {
            cart.setFinalPrice(cart.getFinalPrice() + productDetail.getPrice());
        }
    }

    private void handleBuyXGetYFree(ProductDetail productDetail, Promotion promotion, long count) {
        cart.setTotalPrice(cart.getTotalPrice() + productDetail.getPrice());
        cart.setFinalPrice(cart.getFinalPrice() + productDetail.getPrice());
        if (count % (promotion.getRequiredQty() + promotion.getFreeQty()) == 0) {
            cart.setFinalPrice(cart.getFinalPrice() - productDetail.getPrice());
        }
    }

    private void handleFlatPercent(ProductDetail productDetail, Promotion promotion) {
        int discountAmount = (int) (productDetail.getPrice() * (promotion.getAmount() / 100.0));
        cart.setTotalPrice(cart.getTotalPrice() + productDetail.getPrice());
        cart.setFinalPrice(cart.getFinalPrice() + (productDetail.getPrice() - discountAmount));
    }

}
