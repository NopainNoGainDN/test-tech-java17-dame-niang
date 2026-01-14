package com.tech.cart.controller;

import com.tech.cart.CartService.CartService;
import com.tech.cart.domain.Cart;
import com.tech.cart.domain.Offer;
import com.tech.cart.dto.AddItemRequest;
import com.tech.cart.dto.CartLineResponse;
import com.tech.cart.dto.CartResponse;
import com.tech.cart.exception.BusinessException;
import com.tech.cart.exception.NotFoundException;
import com.tech.cart.repository.CartRepository;
import com.tech.cart.repository.ProductRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    // Ajouter ou modifier un article
    @PostMapping("/items")
    public ResponseEntity<Void> addItem(
            @PathVariable String userId,
            @Valid @RequestBody AddItemRequest request
    ) {
        try {
            cartService.addItem(userId, request.getProductId(),
                    request.getOfferId(), request.getQuantity());
            return ResponseEntity.ok().build();
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Supprimer un article
    @DeleteMapping("/items/{productId}/{offerId}")
    public ResponseEntity<Void> removeItem(
            @PathVariable String userId,
            @PathVariable String productId,
            @PathVariable String offerId
    ) {
        Cart cart = cartRepository.getOrCreate(userId);
        String key = productId + ":" + offerId;
        cart.remove(key);
        return ResponseEntity.ok().build();
    }

    // Consulter le panier
    @GetMapping
    public ResponseEntity<CartResponse> getCart(@PathVariable String userId) {
        Cart cart = cartRepository.getOrCreate(userId);

        List<CartLineResponse> lines = cart.getLines().stream().map(line -> {
            Offer offer = productRepository.findOffer(line.productId(), line.offerId())
                    .orElseThrow(() ->
                            new NotFoundException("Offre introuvable pour le produit " + line.productId()));
            return new CartLineResponse(
                    line.productId(),
                    line.offerId(),
                    offer.finalPrice(),
                    line.quantity()
            );
        }).toList();

        return ResponseEntity.ok(new CartResponse(lines));
    }
}
