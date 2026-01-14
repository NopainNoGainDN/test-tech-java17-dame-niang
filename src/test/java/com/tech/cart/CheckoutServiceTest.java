package com.tech.cart;

import com.tech.cart.CartService.CheckoutService;
import com.tech.cart.domain.Cart;
import com.tech.cart.domain.CartLine;
import com.tech.cart.domain.Offer;
import com.tech.cart.domain.OfferState;
import com.tech.cart.exception.BusinessException;
import com.tech.cart.exception.NotFoundException;
import com.tech.cart.repository.CartRepository;
import com.tech.cart.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CheckoutServiceTest {

    private CartRepository cartRepository;
    private ProductRepository productRepository;
    private CheckoutService checkoutService;

    @BeforeEach
    void setup() {
        cartRepository = new CartRepository();
        productRepository = mock(ProductRepository.class);
        checkoutService = new CheckoutService(cartRepository, productRepository);
    }

    @Test
    void checkout_success_shouldDecrementStockAndClearCart() {
        // Préparer l'offre
        Offer offer = new Offer("O1", "P1", OfferState.NEUF, BigDecimal.valueOf(100), 0, 5);
        when(productRepository.findOffer("P1", "O1")).thenReturn(java.util.Optional.of(offer));

        // Préparer le panier
        Cart cart = cartRepository.getOrCreate("user1");
        cart.addOrUpdate("P1:O1", new CartLine("P1", "O1", 3));

        // Exécuter le checkout
        checkoutService.checkout("user1");

        // Vérifier que le stock a été décrémenté
        assertEquals(2, offer.getStockQty());

        // Vérifier que le panier est vidé
        assertTrue(cart.getLines().isEmpty());
    }

    @Test
    void checkout_offerNotFound_shouldThrow() {
        when(productRepository.findOffer("P1", "O1")).thenReturn(java.util.Optional.empty());

        Cart cart = cartRepository.getOrCreate("user1");
        cart.addOrUpdate("P1:O1", new CartLine("P1", "O1", 1));

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                checkoutService.checkout("user1")
        );

        assertEquals("Offre introuvable pour le produit P1", exception.getMessage());
    }

    @Test
    void checkout_stockInsufficient_shouldThrow() {
        Offer offer = new Offer("O1", "P1", OfferState.NEUF, BigDecimal.valueOf(100), 0, 2);
        when(productRepository.findOffer("P1", "O1")).thenReturn(java.util.Optional.of(offer));

        Cart cart = cartRepository.getOrCreate("user1");
        cart.addOrUpdate("P1:O1", new CartLine("P1", "O1", 3));

        BusinessException exception = assertThrows(BusinessException.class, () ->
                checkoutService.checkout("user1")
        );

        assertEquals("Stock insuffisant", exception.getMessage());
    }
}