package com.tech.cart;

import com.tech.cart.CartService.CartService;
import com.tech.cart.domain.Cart;
import com.tech.cart.domain.Offer;
import com.tech.cart.domain.OfferState;
import com.tech.cart.exception.BusinessException;
import com.tech.cart.repository.CartRepository;
import com.tech.cart.repository.ProductRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartServiceTest {

    private CartRepository cartRepository;
    private ProductRepository productRepository;
    private CartService cartService;

    @BeforeEach
    void setup() {
        cartRepository = new CartRepository();
        productRepository = mock(ProductRepository.class);
        cartService = new CartService(cartRepository, productRepository);
    }

    @Test
    void addItem_success() {
        Offer offer = new Offer("O1", "P1", OfferState.NEUF, BigDecimal.valueOf(100), 10, 5);
        when(productRepository.findOffer("P1", "O1")).thenReturn(Optional.of(offer));

        cartService.addItem("user1", "P1", "O1", 2);

        Cart cart = cartRepository.getOrCreate("user1");
        assertEquals(1, cart.getLines().size());
        assertEquals(2, cart.getLines().iterator().next().quantity());
    }

    @Test
    void addItem_stockZero_shouldThrow() {
        Offer offer = new Offer("O1", "P1", OfferState.NEUF, BigDecimal.valueOf(100), 0, 0);
        when(productRepository.findOffer("P1", "O1")).thenReturn(Optional.of(offer));

        BusinessException exception = assertThrows(BusinessException.class, () ->
                cartService.addItem("user1", "P1", "O1", 1)
        );

        assertEquals("Stock épuisé pour cette offre", exception.getMessage());
    }

    @Test
    void addItem_offerNotFound_shouldThrow() {
        when(productRepository.findOffer("P1", "O1")).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () ->
                cartService.addItem("user1", "P1", "O1", 1)
        );

        assertEquals("Offre introuvable", exception.getMessage());
    }
}