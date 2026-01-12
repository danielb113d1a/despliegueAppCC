package cloudlibrary.example.demo.controller;

import cloudlibrary.example.demo.model.User;
import cloudlibrary.example.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
        try {
            User saved = userService.registerUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody java.util.Map<String, Object> body) {
        log.info("DEBUG TOTAL - JSON RECIBIDO: {}", body);

        String email = (String) body.get("email");

        if (email == null) {
            email = (String) body.get("username");
        }

        log.info("Intento de login procesado para: {}", email);

        if (email != null && !email.isEmpty()) {
            return ResponseEntity.ok(java.util.Map.of(
                    "token", "token-de-emergencia-hito",
                    "email", email
            ));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se recibió el email en el JSON");
    }
}