package com.tech.cart.controller;

import com.tech.cart.CartService.CheckoutService;
import com.tech.cart.exception.BusinessException;
import com.tech.cart.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users/{userId}/checkout")
@RequiredArgsConstructor
public class CheckoutController {

    private final CheckoutService checkoutService;

    // Valider le panier (checkout)
    @PostMapping
    public ResponseEntity<String> checkout(@PathVariable String userId) {
        try {
            checkoutService.checkout(userId);
            return ResponseEntity.ok("Commande validée avec succès !");
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erreur inattendue");
        }
    }
}