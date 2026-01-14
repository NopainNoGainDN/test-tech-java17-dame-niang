package com.tech.cart.domain;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class Cart {
    private final String userId;
    private final Map<String, CartLine> lines = new HashMap<>();

    public Cart(String userId) {
        this.userId = userId;
    }

    public void addOrUpdate(String key, CartLine line) {
        lines.put(key, line);
    }

    public void remove(String key) {
        lines.remove(key);
    }

    public void clear() {
        lines.clear();
    }

    public Collection<CartLine> getLines() {
        return lines.values();
    }
}
