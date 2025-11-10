package cloudlibrary.example.demo.controller;

import cloudlibrary.example.demo.model.User;
import cloudlibrary.example.demo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        log.info("Request POST /api/users/register - Intento de registro para el email: {}", user.getEmail());

        try {
            User saved = userService.registerUser(user);
            log.info("Usuario registrado exitosamente con ID: {}", saved.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (IllegalArgumentException e) {
            log.warn("Intento de registro fallido (email duplicado): {}", user.getEmail());
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // Usuario ya existe
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Boolean> authenticate(
            @RequestParam String email,
            @RequestParam String password) {

        log.info("Request POST /api/users/login - Intento de autenticación para: {}", email);

        try {
            boolean authenticated = userService.authenticate(email, password);
            if(authenticated) {
                log.info("Autenticación exitosa para: {}", email);
            } else {
                log.warn("Autenticación fallida para: {}", email);
            }
            return ResponseEntity.ok(authenticated);
        } catch (UnsupportedOperationException e) {
            log.error("El método de autenticación no está implementado", e);
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(false);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getProfile(@PathVariable Long id) {
        log.info("Request GET /api/users/{} - Solicitando perfil de usuario", id);

        try {
            User profile = userService.getProfile(id);
            return ResponseEntity.ok(profile);
        } catch (UnsupportedOperationException e) {
            log.error("El método getProfile no está implementado", e);
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
        }
    }
}