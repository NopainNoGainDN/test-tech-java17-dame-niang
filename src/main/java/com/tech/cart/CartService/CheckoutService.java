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
public class CheckoutService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CheckoutService(CartRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    public void checkout(String userId) {
        Cart cart = cartRepository.getOrCreate(userId);

        // Vérifier stock
        for (CartLine line : cart.getLines()) {
            Offer offer = productRepository
                    .findOffer(line.productId(), line.offerId())
                    .orElseThrow(() ->
                            new NotFoundException(
                                    "Offre introuvable pour le produit " + line.productId()
                            )
                    );

            if (offer.getStockQty() < line.quantity()) {
                throw new BusinessException("Stock insuffisant");
            }
        }

        // Décrémenter le stock
        for (CartLine line : cart.getLines()) {
            Offer offer = productRepository
                    .findOffer(line.productId(), line.offerId())
                    .orElseThrow(); // safe ici car déjà vérifié

            offer.decreaseStock(line.quantity());
        }

        // Vider le panier
        cart.clear();
    }
}
