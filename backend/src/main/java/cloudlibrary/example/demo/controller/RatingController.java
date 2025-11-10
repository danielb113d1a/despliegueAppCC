package cloudlibrary.example.demo.controller;

import cloudlibrary.example.demo.model.Rating;
import cloudlibrary.example.demo.service.RatingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    private static final Logger log = LoggerFactory.getLogger(RatingController.class);

    private final RatingService ratingService;
    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<Rating>> getRatingsByBook(@PathVariable Long bookId) {
        log.info("Request GET /api/ratings/book/{} - Solicitando valoraciones para el libro", bookId);
        return ResponseEntity.ok(ratingService.getRatingsByBook(bookId));
    }

    @PostMapping
    public ResponseEntity<Rating> addRating(@RequestBody Rating rating) {
        log.info("Request POST /api/ratings - Añadiendo valoración de {} estrellas para el libro {}", rating.getValue(), rating.getBook().getId());
        return ResponseEntity.ok(ratingService.addRating(rating));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRating(@PathVariable Long id) {
        log.info("Request DELETE /api/ratings/{} - Eliminando valoración", id);
        ratingService.deleteRating(id);
        return ResponseEntity.noContent().build();
    }
}