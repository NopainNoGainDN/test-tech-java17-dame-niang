package com.tech.cart.repository;

import com.tech.cart.domain.Offer;
import com.tech.cart.domain.Product;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ProductRepository {
    private final Map<String, Product> products = new ConcurrentHashMap<>();

    public Optional<Offer> findOffer(String productId, String offerId) {
        return Optional.ofNullable(products.get(productId))
                .flatMap(p -> p.findOffer(offerId));
    }

    public void addProduct(Product product) {
        products.put(product.productId(), product);
    }
}
