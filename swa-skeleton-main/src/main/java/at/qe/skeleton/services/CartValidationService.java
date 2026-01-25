package at.qe.skeleton.services;

import at.qe.skeleton.dtos.CartDTO;
import at.qe.skeleton.dtos.CartItemDTO;
import at.qe.skeleton.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
public class CartValidationService {

    private final ProductService productService;

    @Autowired
    public CartValidationService(ProductService productService) {
        this.productService = productService;
    }

    public Optional<CartDTO> validateCart(CartDTO cart) {
        Collection<CartItemDTO> updatedItems = new ArrayList<>();

        for (CartItemDTO item : cart.items()) {
            Optional<Product> optionalProduct = productService.getProductById(item.productId());

            if (optionalProduct.isEmpty()) {
                return Optional.empty();
            }

            Product product = optionalProduct.get();

            Double actualPrice = product.getPrice() * (1.0 - product.getDiscount());

            if (product.getStock() < item.amount()) {
                return Optional.empty();
            }

            updatedItems.add(new CartItemDTO(
                    item.productId(),
                    item.productName(),
                    item.productImage(),
                    actualPrice,
                    item.amount()
            ));
        }

        CartDTO updatedCart = new CartDTO(updatedItems);
        return Optional.of(updatedCart);
    }
}