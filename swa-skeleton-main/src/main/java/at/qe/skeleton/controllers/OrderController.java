package at.qe.skeleton.controllers;

import at.qe.skeleton.dtos.OrderDTO;
import at.qe.skeleton.exceptions.InsufficientStockException;
import at.qe.skeleton.exceptions.OrderNotFoundException;
import at.qe.skeleton.exceptions.UnauthorizedOrderAccessException;
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
    public ResponseEntity<?> createOrder(@RequestBody at.qe.skeleton.dtos.CheckoutRequestDTO checkoutRequest) {
        Userx currentUser = authenticatedUserService.getAuthenticatedUser();
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            OrderDTO orderDto = orderService.placeOrder(currentUser, checkoutRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(orderDto);
        } catch (InsufficientStockException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
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
        } catch (OrderNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (UnauthorizedOrderAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
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
        } catch (OrderNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (UnauthorizedOrderAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}