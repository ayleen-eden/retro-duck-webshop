package at.qe.skeleton.dtos;

import at.qe.skeleton.model.NotificationType;

import java.time.LocalDateTime;

public record NotificationDTO(
    Long id,
    Long productId,
    Long userId,
    String description,
    String title,
    LocalDateTime timestamp,
    NotificationType type
) { }