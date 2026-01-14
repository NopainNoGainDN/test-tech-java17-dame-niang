package com.tech.cart.CartService;

import com.tech.cart.domain.Cart;
import com.tech.cart.domain.CartLine;
import com.tech.cart.domain.Offer;
import com.tech.cart.exception.BusinessException;
import com.tech.cart.exception.NotFoundException;
import com.tech.cart.repository.CartRepository;
import com.tech.cart.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    public void addItem(String userId, String productId, String offerId, int qty) {
        Offer offer = productRepository.findOffer(productId, offerId)
                .orElseThrow(() -> new NotFoundException("Offre introuvable"));

        if (offer.getStockQty() <= 0) {
            throw new BusinessException("Stock épuisé pour cette offre");
        }

        Cart cart = cartRepository.getOrCreate(userId);
        cart.addOrUpdate(productId + ":" + offerId, new CartLine(productId, offerId, qty));
    }
}
