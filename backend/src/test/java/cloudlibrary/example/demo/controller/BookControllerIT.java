package cloudlibrary.example.demo.controller;

import cloudlibrary.example.demo.model.Book;
import cloudlibrary.example.demo.model.Rating;
import cloudlibrary.example.demo.model.User;
import cloudlibrary.example.demo.repository.BookRepository;
import cloudlibrary.example.demo.repository.RatingRepository;
import cloudlibrary.example.demo.repository.UserRepository;

import com.fasterxml.jackson.databind.ObjectMapper; // Para convertir objetos a JSON
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class BookControllerIT {

    @Autowired
    private MockMvc mockMvc; // La herramienta para simular peticiones HTTP

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper; // Para convertir objetos a JSON string

    private Book testBook;
    private User testUser;
    private String testUsername = "daniel";

    @BeforeEach
    void setUp() {
        ratingRepository.deleteAll();
        bookRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User();
        user.setUsername(testUsername);
        user.setEmail("testuser@example.com");
        user.setPassword(passwordEncoder.encode("password123"));

        testUser = userRepository.save(user);

        Book book = new Book();
        book.setTitle("Libro de Test de Integración");
        book.setAuthor("Autor Test");
        testBook = bookRepository.save(book);
    }

    @Test
    void shouldGetAllBooks() throws Exception {
        mockMvc.perform(get("/api/books")
                        .with(user(testUsername)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("Libro de Test de Integración")));
    }

    @Test
    void shouldGetBookById() throws Exception {
        mockMvc.perform(get("/api/books/" + testBook.getId())
                        .with(user(testUsername)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testBook.getId().intValue())))
                .andExpect(jsonPath("$.title", is(testBook.getTitle())));
    }

    @Test
    void shouldReturnNotFoundForInvalidId() throws Exception {
        mockMvc.perform(get("/api/books/99999")
                        .with(user(testUsername)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldAddNewBook() throws Exception {
        Book newBook = new Book();
        newBook.setTitle("Nuevo Libro");
        newBook.setAuthor("Nuevo Autor");
        String bookJson = objectMapper.writeValueAsString(newBook);

        mockMvc.perform(post("/api/books")
                        .with(user(testUsername))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.title", is("Nuevo Libro")));
    }

    @Test
    void shouldDeleteBook() throws Exception {
        mockMvc.perform(delete("/api/books/" + testBook.getId())
                        .with(user(testUsername))
                        .with(csrf()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/books/" + testBook.getId())
                        .with(user(testUsername)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldGetAverageRating_WhenNoRatings_ReturnsZero() throws Exception {
        mockMvc.perform(get("/api/books/" + testBook.getId() + "/average-rating")
                        .with(user(testUsername)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(0.0)));
    }

    @Test
    void shouldGetAverageRating_WhenRatingsExist() throws Exception {
        Rating r1 = new Rating();
        r1.setBook(testBook);
        r1.setUser(testUser);
        r1.setValue(5);
        ratingRepository.save(r1);

        Rating r2 = new Rating();
        r2.setBook(testBook);
        r2.setUser(testUser);
        r2.setValue(3);
        ratingRepository.save(r2);

        mockMvc.perform(get("/api/books/" + testBook.getId() + "/average-rating")
                        .with(user(testUsername)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(4.0)));
    }

    @Test
    void shouldGetAverageRating_WhenBookNotFound() throws Exception {
        mockMvc.perform(get("/api/books/99999/average-rating")
                        .with(user(testUsername)))
                .andExpect(status().isNotFound());
    }
}