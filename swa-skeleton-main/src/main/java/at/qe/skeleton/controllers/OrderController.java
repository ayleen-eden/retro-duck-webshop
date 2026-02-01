package at.qe.skeleton.controllers;

import at.qe.skeleton.dtos.CheckoutRequestDTO;
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

/**
 * REST Controller for managing order-related operations.
 * * Provides an API for users to view their order history, retrieve specific
 * order details, and place new orders via the checkout process.
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private AuthenticatedUserService authenticatedUserService;

    /**
     * Retrieves the complete order history for the currently authenticated user.
     * * @return a {@link ResponseEntity} containing a collection of {@link OrderDTO}s.
     */
    @GetMapping("/")
    public ResponseEntity<Collection<OrderDTO>> getAllOrders() {
        Userx currentUser = authenticatedUserService.getAuthenticatedUser();
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(orderService.getOrderHistory(currentUser));
    }

    /**
     * Processes a checkout request and creates a new order.
     * * @param checkoutRequest DTO containing shipping and payment information.
     * @return a {@link ResponseEntity} with the created {@link OrderDTO} or an error message.
     */
    @PostMapping("/")
    public ResponseEntity<Object> createOrder(@RequestBody CheckoutRequestDTO checkoutRequest) {
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

    /**
     * Retrieves a specific order by its unique identifier.
     * <p>
     * Ownership is verified within the service layer to ensure users can only
     * view their own orders.
     *
     * @param id the unique ID of the order to retrieve.
     * @return a {@link ResponseEntity} containing the {@link OrderDTO}.
     * Returns {@code 404 Not Found} with an error message if {@link OrderNotFoundException} occurs,
     * or {@code 403 Forbidden} with a message if {@link UnauthorizedOrderAccessException} is thrown.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> getOrderById(@PathVariable Long id) {
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

    /**
     * Deletes a specific order from the system.
     * <p>
     * This operation is only permitted if the order belongs to the authenticated user.
     *
     * @param id the unique ID of the order to delete.
     * @return a {@link ResponseEntity} with {@code 204 No Content} on success.
     * Returns {@code 404 Not Found} if {@link OrderNotFoundException} is caught,
     * or {@code 403 Forbidden} if {@link UnauthorizedOrderAccessException} is caught.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteOrder(@PathVariable Long id) {
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