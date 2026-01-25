package at.qe.skeleton.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CheckoutRequestDTO(
        @NotNull
        CartDTO cart,
        @NotBlank
        String shippingName,
        @NotBlank
        String shippingStreet,
        @NotBlank
        String shippingCity,
        @NotBlank
        String shippingPostalCode,
        @NotBlank
        String shippingCountry,
        @NotBlank
        String paymentMethod
) {
}