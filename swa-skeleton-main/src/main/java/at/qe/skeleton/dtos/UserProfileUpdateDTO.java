package at.qe.skeleton.dtos;

import at.qe.skeleton.services.NotificationChannelType;

import java.util.Set;

public record UserProfileUpdateDTO(
        String firstName,
        String lastName,
        String email,
        String phone,
        String password,
        Set<NotificationChannelType> preferredChannels
) {}