package cloudlibrary.example.demo.controller;

import cloudlibrary.example.demo.model.Book;
import cloudlibrary.example.demo.model.Rating;
import cloudlibrary.example.demo.model.User;
import cloudlibrary.example.demo.repository.RatingRepository;
import cloudlibrary.example.demo.service.BookService;
import cloudlibrary.example.demo.service.RatingService;
import cloudlibrary.example.demo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    private static final Logger log = LoggerFactory.getLogger(RatingController.class);

    private final RatingService ratingService;
    private final UserService userService;
    private final BookService bookService;
    private final RatingRepository ratingRepository;

    public RatingController(RatingService ratingService, UserService userService, BookService bookService, RatingRepository ratingRepository) {
        this.ratingService = ratingService;
        this.userService = userService;
        this.bookService = bookService;
        this.ratingRepository = ratingRepository;
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<Rating>> getRatingsByBook(@PathVariable Long bookId) {
        log.info("Request GET /api/ratings/book/{} - Solicitando valoraciones", bookId);
        return ResponseEntity.ok(ratingService.getRatingsByBook(bookId));
    }

    @PostMapping
    public ResponseEntity<?> addRating(@RequestBody Map<String, Object> ratingData) {
        try {
            Integer value = (Integer) ratingData.get("value");
            String email = (String) ratingData.get("email");
            Long bookId = Long.valueOf(ratingData.get("bookId").toString());

            log.info("Usuario {} valorando libro {} con {} estrellas", email, bookId, value);

            User user = userService.getUserByEmail(email);
            Book book = bookService.getBookById(bookId)
                    .orElseThrow(() -> new RuntimeException("Libro no encontrado con ID: " + bookId));

            Rating rating = new Rating();
            rating.setValue(value);
            rating.setUser(user);
            rating.setBook(book);

            return ResponseEntity.ok(ratingService.addRating(rating));

        } catch (Exception e) {
            log.error("Error al añadir valoración", e);
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRating(@PathVariable Long id) {
        ratingService.deleteRating(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{email}")
    public ResponseEntity<List<Rating>> getUserRatings(@PathVariable String email) {
        log.info("Request GET /api/ratings/user/{} - Solicitando votos del usuario", email);
        return ResponseEntity.ok(ratingRepository.findByUser_Email(email));
    }
}