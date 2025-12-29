package at.qe.skeleton.dtos;

import java.util.Collection;

public record CartDTO(
        Collection<CartItemDTO> items
) {}
