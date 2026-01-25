package at.qe.skeleton.controllers;

import at.qe.skeleton.dtos.UserProfileUpdateDTO;
import at.qe.skeleton.dtos.UserxDTO;
import at.qe.skeleton.exceptions.UsernameDuplicateException;
import at.qe.skeleton.mappers.UserxMapper;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.services.UserxService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import at.qe.skeleton.dtos.UserxCreateDTO;

import java.util.Map;

/**
 * Userx endpoints exposed by the server.
 * <p>
 * This class is part of the skeleton project provided for students of the
 * course "Software Architecture" offered by Innsbruck University.
 */
@RestController
@RequestMapping("/api/users")
public class UserxController {

    private final UserxMapper userMapper;
    private final UserxService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserxController.class);

    @Autowired
    public UserxController(UserxMapper userMapper, UserxService userService) {
        this.userMapper = userMapper;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody UserxCreateDTO createDTO) {
        try {
            Userx newUser = new Userx();
            newUser.setUsername(createDTO.username());
            newUser.setFirstName(createDTO.firstName());
            newUser.setLastName(createDTO.lastName());
            newUser.setEmail(createDTO.email());
            newUser.setPhone(createDTO.phone());
            newUser.setEnabled(true);
            newUser.setPassword(createDTO.password());

            Userx savedUser = userService.saveUser(newUser);

            return ResponseEntity.ok(userMapper.mapTo(savedUser));

        } catch (UsernameDuplicateException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected error during user registration", e);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "An unexpected error occurred: " + e.getMessage()));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UserxDTO> getCurrentUser(@AuthenticationPrincipal Userx user) {
        return ResponseEntity.ok(userMapper.mapTo(user));
    }

    @PatchMapping("/me")
    public ResponseEntity<UserxDTO> updateCurrentUser(@AuthenticationPrincipal Userx currentUser, @RequestBody UserProfileUpdateDTO dto) {
        if (currentUser == null) {
            return ResponseEntity.status(401).build();
        }
        try {
            Userx updatedUser = userService.updateUserSelf(currentUser, dto);
            return ResponseEntity.ok(userMapper.mapTo(updatedUser));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserxDTO> getUser(@PathVariable Long id) {
        try {
            Userx user = userService.getUserById(id);
            return ResponseEntity.ok(userMapper.mapTo(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/authenticated")
    public ResponseEntity<String> isAuthenticated(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body("User not authenticated");
        }
        return ResponseEntity.ok("User is authenticated: " + userDetails.getUsername());
    }
}
