package cloudlibrary.example.demo.controller;

import cloudlibrary.example.demo.model.Book;
import cloudlibrary.example.demo.model.Category;
import cloudlibrary.example.demo.model.User;
import cloudlibrary.example.demo.repository.CategoryRepository;
import cloudlibrary.example.demo.service.BookService;
import cloudlibrary.example.demo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;
    private final UserService userService;
    private final CategoryRepository categoryRepository;

    public BookController(BookService bookService, UserService userService, CategoryRepository categoryRepository) {
        this.bookService = bookService;
        this.userService = userService;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.findAllBooks();
    }

    @PostMapping
    public ResponseEntity<?> createBook(@RequestBody Map<String, Object> bookData) {
        try {
            String title = (String) bookData.get("title");
            String author = (String) bookData.get("author");
            String description = (String) bookData.get("description");
            String email = (String) bookData.get("email");

            Object catIdObj = bookData.get("categoryId");
            Category category = null;

            if (catIdObj != null) {
                Long catId = Long.valueOf(catIdObj.toString());
                category = categoryRepository.findById(catId).orElse(null);
            }

            if (email == null) {
                return ResponseEntity.badRequest().body("Error: El email es obligatorio");
            }

            User owner = userService.getUserByEmail(email);

            Book newBook = new Book();
            newBook.setTitle(title);
            newBook.setAuthor(author);
            newBook.setDescription(description);
            newBook.setUser(owner);
            newBook.setCategory(category);

            return ResponseEntity.ok(bookService.saveBook(newBook));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error al crear libro: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}