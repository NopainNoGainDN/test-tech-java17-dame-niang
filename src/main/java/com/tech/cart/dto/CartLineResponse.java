package com.tech.cart.dto;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class CartLineResponse {
    private final String productId;
    private final String offerId;
    private final BigDecimal unitPrice;
    private final int quantity;
    private final BigDecimal lineTotal;

    public CartLineResponse(
            String productId,
            String offerId,
            BigDecimal unitPrice,
            int quantity
    ) {
        this.productId = productId;
        this.offerId = offerId;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.lineTotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

}
