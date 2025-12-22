package at.qe.skeleton.controllers;

import at.qe.skeleton.dtos.CartDTO;
import at.qe.skeleton.dtos.CartItemDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final ProductRepository productRepository;

    /*
    ? What does the response entity mean?
    - Function returns TRUE if the cart is valid
    - Function returns FALSE if the cart is unvalid and needs an update
    ? When is a cart valid?
    - Prices and discounts match
    - Product exists
    - Enough product is available
     */
    @PostMapping("/validate")
    public ResponseEntity<CartDTO> validateCart(@RequestBody CartDTO cart) {
        Collection<CartItemDTO> updatedItems = new ArrayList<>();

        for (CartItemDTO item : cart.items()) {
            Optional<Product> optionalProduct = productRepository.findById(item.productId());

            if (optionalProduct.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Product product = optionalProduct.get();
            Double actualPrice = product.getPrice() * product.getDiscount();

            if (product.getStock() < item.amount()) {
                return ResponseEntity.badRequest().build();
            } else if (!actualPrice.equals(item.pricePerUnit())) { // ? How is discount implemented
                CartItemDTO updatedItem = actualPrice.equals(item.pricePerUnit())
                        ? item
                        : new CartItemDTO(item.productId(), actualPrice, item.amount());
                updatedItems.add(updatedItem);
            }
        }

        CartDTO updatedCart = new CartDTO(updatedItems);

        return ResponseEntity.ok(updatedCart);
    }

}
