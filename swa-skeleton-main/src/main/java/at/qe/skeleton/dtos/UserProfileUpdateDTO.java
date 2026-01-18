package at.qe.skeleton.dtos;

public record UserProfileUpdateDTO(
        String firstName,
        String lastName,
        String email,
        String phone,
        String password
) {}