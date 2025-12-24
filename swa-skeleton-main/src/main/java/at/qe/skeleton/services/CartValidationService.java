package at.qe.skeleton.services;

import at.qe.skeleton.dtos.CartDTO;
import at.qe.skeleton.dtos.CartItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
public class CartValidationService {

    private final ProductRepository productRepository;

    @Autowired
    public CartValidationService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /*
    ? When is a cart valid?
    - Prices and discounts match
    - Product exists
    - Enough product is available
    */

    public Optional<CartDTO> validateCart(CartDTO cart) {
        Collection<CartItemDTO> updatedItems = new ArrayList<>();

        for (CartItemDTO item : cart.items()) {
            Optional<Product> optionalProduct = productRepository.findById(item.productId());

            if (optionalProduct.isEmpty()) {
                return Optional.empty();
            }

            Product product = optionalProduct.get();
            Double actualPrice = product.getPrice() * product.getDiscount();

            if (product.getStock() < item.amount()) {
                return Optional.empty();
            }

            if (actualPrice.equals(item.pricePerUnit())) { // ? How is discount implemented
                updatedItems.add(item);
            } else {
                updatedItems.add(new CartItemDTO(item.productId(), actualPrice, item.amount()));
            }
        }

        CartDTO updatedCart = new CartDTO(updatedItems);

        return Optional.of(updatedCart);
    }
}
