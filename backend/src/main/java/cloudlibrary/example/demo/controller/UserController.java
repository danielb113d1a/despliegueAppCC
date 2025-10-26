package cloudlibrary.example.demo.controller;

import cloudlibrary.example.demo.model.User;
import cloudlibrary.example.demo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Registrar un nuevo usuario
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        try {
            User saved = userService.registerUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // Usuario ya existe
        }
    }

    // Autenticar usuario pasando email y password directamente
    @PostMapping("/login")
    public ResponseEntity<Boolean> authenticate(
            @RequestParam String email,
            @RequestParam String password) {

        try {
            boolean authenticated = userService.authenticate(email, password);
            return ResponseEntity.ok(authenticated);
        } catch (UnsupportedOperationException e) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(false);
        }
    }

    // Obtener perfil de usuario
    @GetMapping("/{id}")
    public ResponseEntity<User> getProfile(@PathVariable Long id) {
        try {
            User profile = userService.getProfile(id);
            return ResponseEntity.ok(profile);
        } catch (UnsupportedOperationException e) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
        }
    }
}
