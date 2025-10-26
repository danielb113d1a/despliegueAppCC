package cloudlibrary.example.demo.service;

import cloudlibrary.example.demo.model.User;
import cloudlibrary.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Testcontainers
class UserServiceIT {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    static {
        postgres.start();
        System.setProperty("spring.datasource.url", postgres.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgres.getUsername());
        System.setProperty("spring.datasource.password", postgres.getPassword());
    }

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    // Para que el PasswordEncoder se pueda inyectar en la configuración
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        userRepository.deleteAll(); // Limpia la tabla de usuarios
    }

    @Test
    void shouldRegisterNewUser() {
        String email = "user" + System.currentTimeMillis() + "@example.com";
        User user = new User();
        user.setUsername("Daniel");
        user.setEmail(email);
        user.setPassword("123456");

        User saved = userService.registerUser(user);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getUsername()).isEqualTo("Daniel");
    }

    @Test
    void shouldNotRegisterDuplicateEmail() {
        // Primer usuario
        User user1 = new User();
        user1.setUsername("Daniel");
        user1.setEmail("daniel@example.com");
        user1.setPassword("123456");
        userService.registerUser(user1);

        // Segundo usuario con el mismo email
        User user2 = new User();
        user2.setUsername("Otro");
        user2.setEmail("daniel@example.com");
        user2.setPassword("abcdef");

        assertThrows(IllegalArgumentException.class,
                () -> userService.registerUser(user2));
    }

    // Placeholder para la obtención del perfil de usuario
    @Test
    void shouldReturnUserProfile() {
        // given — se crea y guarda un usuario real
        User user = new User();
        user.setUsername("Perfil");
        user.setEmail("perfil" + System.currentTimeMillis() + "@example.com");
        user.setPassword("123");

        User saved = userService.registerUser(user);

        // when — se intenta recuperar su perfil
        User profile = userService.getProfile(saved.getId());

        // then — se comprueba que el perfil devuelto coincide con el guardado
        assertThat(profile).isNotNull();
        assertThat(profile.getId()).isEqualTo(saved.getId());
        assertThat(profile.getEmail()).isEqualTo(saved.getEmail());
        assertThat(profile.getUsername()).isEqualTo(saved.getUsername());
    }

    // Placeholder para cuando implementes la autenticación
    @Test
    void shouldAuthenticateUser() {
        String password = "pass";

        User user = new User();
        user.setUsername("Test");
        user.setEmail("test" + System.currentTimeMillis() + "@example.com");
        user.setPassword(password);

        userService.registerUser(user);

        boolean exitoso = userService.authenticate(user.getEmail(), password);

        assertThat(exitoso).isTrue();
    }
}