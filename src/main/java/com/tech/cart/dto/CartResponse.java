package com.tech.cart.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class CartResponse {
    private final List<CartLineResponse> lines;
    private final BigDecimal total;

    public CartResponse(List<CartLineResponse> lines) {
        this.lines = lines;
        this.total = lines.stream()
                .map(CartLineResponse::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
