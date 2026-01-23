package at.qe.skeleton.services;

import at.qe.skeleton.model.Userx;
import at.qe.skeleton.repositories.UserxRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@WebAppConfiguration
public class UserxServiceSignUpTest {

    @Mock
    private UserxRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticatedUserService authenticatedUserService;

    @InjectMocks
    private UserxService userxService;

    @Test
    public void testSignUpSuccess() {
        Userx newUser = new Userx();
        newUser.setUsername("new_user");
        newUser.setPassword("raw_password");

        when(userRepository.existsByUsername("new_user")).thenReturn(false);
        when(passwordEncoder.encode("raw_password")).thenReturn("hashed_password");
        when(authenticatedUserService.getAuthenticatedUser()).thenReturn(null);
        when(userRepository.save(any(Userx.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Userx savedUser = userxService.saveUser(newUser);

        Assertions.assertNotNull(savedUser, "User should not be null after saving");
        Assertions.assertEquals("hashed_password", savedUser.getPassword(), "Password should be encoded");
        Assertions.assertNull(savedUser.getCreateUser(), "CreateUser should be null for self-signup");

        verify(userRepository, times(1)).save(any(Userx.class));
    }

    @Test
    public void testSignUpWithDuplicateUsername() {
        Userx newUser = new Userx();
        newUser.setUsername("already_exists");

        when(userRepository.existsByUsername("already_exists")).thenReturn(true);

        Assertions.assertThrows(RuntimeException.class, () -> {
            userxService.saveUser(newUser);
        }, "Should throw exception when username is already taken");

        verify(userRepository, never()).save(any(Userx.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testUserCreationByAdmin() {
        Userx admin = new Userx();
        admin.setUsername("admin");

        Userx newUser = new Userx();
        newUser.setUsername("created_by_admin");
        newUser.setPassword("1234");

        when(userRepository.existsByUsername("created_by_admin")).thenReturn(false);
        when(authenticatedUserService.getAuthenticatedUser()).thenReturn(admin);
        when(userRepository.save(any(Userx.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Userx savedUser = userxService.saveUser(newUser);

        Assertions.assertEquals(admin, savedUser.getCreateUser(), "Admin should be set as creator");
    }
}