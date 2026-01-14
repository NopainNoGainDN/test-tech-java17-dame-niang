package com.tech.cart.domain;

import com.tech.cart.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@AllArgsConstructor
public class Offer {

    private final String offerId;
    private final String productId;
    private final OfferState state;
    private final BigDecimal price;
    private final int discountPercent;
    private int stockQty;

    public BigDecimal finalPrice() {
        BigDecimal discountFactor =
                BigDecimal.valueOf(100 - discountPercent)
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        return price.multiply(discountFactor);
    }

    public void decreaseStock(int qty) {
        if (stockQty < qty) {
            throw new BusinessException("Stock insuffisant");
        }
        stockQty -= qty;
    }
}
