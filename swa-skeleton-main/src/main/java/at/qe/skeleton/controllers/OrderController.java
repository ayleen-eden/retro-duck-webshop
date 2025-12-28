package at.qe.skeleton.controllers;

import at.qe.skeleton.dtos.CartDTO;
import at.qe.skeleton.dtos.CartItemDTO;
import at.qe.skeleton.services.CartValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final CartValidationService cartValidationService;

    @Autowired
    public OrderController(CartValidationService cartValidationService) {
        this.cartValidationService = cartValidationService;
    }

    /*
    ! Please adjust method according to the implementation of `Order`
    * All cart-related classes that are still needed here can be found in feature/shopping-cart
    */

    @PostMapping("/checkout")
    public ResponseEntity<Order> checkout(@RequestBody CartDTO cart) {
        Optional<CartDTO> validCart = cartValidationService.validateCart(cart);

        if (validCart.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        CartDTO finalCart = validCart.get();
        Order order = new Order();

        for (CartItemDTO item : finalCart.items()) {

            /*
            ! Create OrderItemDTO from CartItemDTO if necessary
            * addItem() should decrease product stock
            */

            order.addItem(item);
        }

        return ResponseEntity.ok(order);
    }
}
