package at.qe.skeleton.services;

import at.qe.skeleton.dtos.UserProfileUpdateDTO;
import at.qe.skeleton.exceptions.UsernameDuplicateException;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.repositories.UserxRepository;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

/**
 * Service for accessing and manipulating user data.
 * <p>
 * This class is part of the skeleton project provided for students of the
 * course "Software Architecture" offered by Innsbruck University.
 */
@Service
public class UserxService implements UserDetailsService {

    private final UserxRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticatedUserService authenticatedUserService;

    @Autowired
    public UserxService(UserxRepository userRepository, PasswordEncoder passwordEncoder, AuthenticatedUserService authenticatedUserService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticatedUserService = authenticatedUserService;
    }

    /**
     * Returns a collection of all users.
     *
     * @return the userx collection
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Collection<Userx> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Loads a single user identified by its id.
     *
     * @param id the id to search for
     * @return the user with the id
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Optional<Userx> loadUser(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Saves the user. This method will also set {@link Userx#createDate} for new
     * entities or {@link Userx#updateDate} for updated entities. The user
     * requesting this operation will also be stored as {@link Userx#createDate}
     * or {@link Userx#updateUser} respectively.
     *
     * @param user the user to save
     * @return the updated user
     */
    @PermitAll
    public Userx saveUser(Userx user) {
        if (user.isNew()) {
            if (userRepository.existsByUsername(user.getUsername())) {
                throw new UsernameDuplicateException("Username " + user.getUsername() + " not available");
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            Userx authUser = authenticatedUserService.getAuthenticatedUser();
            if (authUser != null) {
                user.setCreateUser(authUser);
            } else {
                user.setCreateUser(null); //sign-up
            }
        } else {
            user.setUpdateUser(authenticatedUserService.getAuthenticatedUser());
        }
        return userRepository.save(user);
    }

    /**
     * Deletes the user.
     *
     * @param user the user to delete
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteUser(Userx user) {
        Optional<Userx> userOpt = userRepository.findById(user.getId());
        userOpt.ifPresent(userRepository::delete);
    }

    public Userx getUserByUsername(String username) {
        return userRepository.findFirstByUsername(username).orElse(null);
    }

    @PreAuthorize("isAuthenticated()")
    public Userx updateUserSelf(Userx currentUser, UserProfileUpdateDTO dto) {
        if (currentUser == null || currentUser.getId() == null) {
            throw new IllegalArgumentException("User or ID missing");
        }

        // Wir laden den User frisch aus der DB über die ID, um sicherzugehen
        Userx user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + currentUser.getId()));

        // Daten updaten, wenn vorhanden
        if (dto.firstName() != null) user.setFirstName(dto.firstName());
        if (dto.lastName() != null) user.setLastName(dto.lastName());
        if (dto.email() != null) user.setEmail(dto.email());
        if (dto.phone() != null) user.setPhone(dto.phone());

        // Passwort nur ändern, wenn es nicht leer ist
        if (dto.password() != null && !dto.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.password()));
        }

        user.setUpdateUser(user); // Metadaten: User hat sich selbst geändert
        return userRepository.save(user);
    }

    /**
     * Loads a user by its username. Required for JWT authentication.
     *
     * @param username the username identifying the user whose data is required.
     * @return the user with the given username and their details.
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findFirstByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
