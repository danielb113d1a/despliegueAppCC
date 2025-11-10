package cloudlibrary.example.demo.controller;

import cloudlibrary.example.demo.model.Book;
import cloudlibrary.example.demo.service.BookService;
import cloudlibrary.example.demo.service.RatingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private static final Logger log = LoggerFactory.getLogger(BookController.class);

    private final BookService bookService;
    private final RatingService ratingService;

    public BookController(BookService bookService, RatingService ratingService) {
        this.bookService = bookService;
        this.ratingService = ratingService;
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        log.info("Request GET /api/books - Solicitando todos los libros");
        List<Book> books = bookService.findAllBooks();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        log.info("Request GET /api/books/{} - Solicitando libro por ID", id);

        return bookService.findBookById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        log.info("Request POST /api/books - Creando nuevo libro con título: {}", book.getTitle());
        Book saved = bookService.saveBook(book);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        log.info("Request DELETE /api/books/{} - Eliminando libro", id);
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/average-rating")
    public ResponseEntity<Double> getAverageRating(@PathVariable Long id) {
        log.info("Request GET /api/books/{}/average-rating - Calculando valoración media", id);

        Optional<Book> book = bookService.findBookById(id);
        if (book.isEmpty()) {
            log.warn("No se encontró el libro con ID {} al calcular la media", id);
            return ResponseEntity.notFound().build();
        }

        Double avg = ratingService.averageRatingForBook(id);
        Double result = (avg != null) ? avg : 0.0;
        return ResponseEntity.ok(result);
    }
}