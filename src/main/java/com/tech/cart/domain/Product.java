package com.tech.cart.domain;

import java.util.List;
import java.util.Optional;

public record Product(String productId, String name, List<Offer> offers) {

    public Optional<Offer> findOffer(String offerId) {
        return offers.stream().filter(o -> o.getOfferId().equals(offerId)).findFirst();
    }
}
