package com.tech.cart.domain;

public record CartLine(
        String productId,
        String offerId,
        int quantity
) {}
