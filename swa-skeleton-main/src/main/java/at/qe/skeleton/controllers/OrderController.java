package at.qe.skeleton.controllers;

import at.qe.skeleton.dtos.CartDTO;
import at.qe.skeleton.dtos.OrderDTO;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.services.OrderService;
import at.qe.skeleton.services.AuthenticatedUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private AuthenticatedUserService authenticatedUserService;

    @GetMapping("/")
    public ResponseEntity<Collection<OrderDTO>> getAllOrders() {
        Userx currentUser = authenticatedUserService.getAuthenticatedUser();
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(orderService.getOrderHistory(currentUser));
    }

    @PostMapping("/")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody CartDTO cartDto) {
        try {
            Userx currentUser = authenticatedUserService.getAuthenticatedUser();
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            OrderDTO orderDto = orderService.placeOrder(currentUser, cartDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(orderDto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        Userx currentUser = authenticatedUserService.getAuthenticatedUser();
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            OrderDTO order = orderService.getOrderById(id, currentUser);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Access denied")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        Userx currentUser = authenticatedUserService.getAuthenticatedUser();
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            orderService.deleteOrder(id, currentUser);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Access denied")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}