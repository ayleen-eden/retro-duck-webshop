package at.qe.skeleton.controllers;

import at.qe.skeleton.dtos.CartDTO;
import at.qe.skeleton.services.CartValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Cart validation endpoints exposed by the server.
 */
@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartValidationService cartValidationService;

    @Autowired
    public CartController(CartValidationService cartValidationService) {
        this.cartValidationService = cartValidationService;
    }

    @PostMapping("/validate")
    public ResponseEntity<CartDTO> validateCart(@RequestBody CartDTO cart) {
        Optional<CartDTO> validCart = cartValidationService.validateCart(cart);

        return validCart.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

}
