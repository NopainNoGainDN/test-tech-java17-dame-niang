package com.tech.cart.repository;

import com.tech.cart.domain.Cart;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class CartRepository {
    private final Map<String, Cart> carts = new ConcurrentHashMap<>();

    public Cart getOrCreate(String userId) {
        return carts.computeIfAbsent(userId, Cart::new);
    }
}
