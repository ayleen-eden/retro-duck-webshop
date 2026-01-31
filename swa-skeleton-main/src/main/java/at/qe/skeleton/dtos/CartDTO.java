package at.qe.skeleton.dtos;

import java.util.Collection;

/**
 * Data transfer object for validating localStorage cart in frontend.
 */
public record CartDTO(
        Collection<CartItemDTO> items
) {}
