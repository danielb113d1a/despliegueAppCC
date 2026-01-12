package cloudlibrary.example.demo.controller;

import cloudlibrary.example.demo.model.User;
import cloudlibrary.example.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("Daniel");
        testUser.setEmail("daniel@example.com");
        testUser.setPassword("123456");
    }

    @Test
    void shouldRegisterNewUser() {
        when(userService.registerUser(any(User.class))).thenReturn(testUser);

        ResponseEntity<User> response = userController.registerUser(testUser);

        assertThat(response).isNotNull();
        assertThat(response.getBody()).isEqualTo(testUser);
        verify(userService, times(1)).registerUser(any(User.class));
    }

    @Test
    void shouldNotRegisterDuplicateEmail() {
        when(userService.registerUser(any(User.class)))
                .thenThrow(new IllegalArgumentException("El usuario ya existe"));

        try {
            userController.registerUser(testUser);
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("El usuario ya existe");
        }

        verify(userService, times(1)).registerUser(any(User.class));
    }


}
