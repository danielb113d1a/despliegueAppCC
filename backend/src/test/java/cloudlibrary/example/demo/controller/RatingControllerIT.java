package cloudlibrary.example.demo.controller;

import cloudlibrary.example.demo.model.Book;
import cloudlibrary.example.demo.model.Rating;
import cloudlibrary.example.demo.model.User;
import cloudlibrary.example.demo.repository.BookRepository;
import cloudlibrary.example.demo.repository.RatingRepository;
import cloudlibrary.example.demo.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class RatingControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private User testUser;
    private Book testBook;
    private Rating testRating;
    private final String testUsername = "testuser";

    @BeforeEach
    void setUp() {
        ratingRepository.deleteAll();
        bookRepository.deleteAll();
        userRepository.deleteAll();

        testUser = new User();
        testUser.setUsername(testUsername);
        testUser.setEmail("test@example.com");
        testUser.setPassword(passwordEncoder.encode("password"));
        userRepository.save(testUser);

        testBook = new Book();
        testBook.setTitle("Libro para Ratings");
        bookRepository.save(testBook);

        testRating = new Rating();
        testRating.setBook(testBook);
        testRating.setUser(testUser);
        testRating.setValue(5);
        ratingRepository.save(testRating);
    }

    @Test
    void shouldGetRatingsByBook() throws Exception {
        mockMvc.perform(get("/api/ratings/book/" + testBook.getId())
                        .with(user(testUsername)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].value", is(5)));
    }

    @Test
    void shouldAddRating() throws Exception {
        Map<String, Object> newRating = Map.of(
                "value", 4,
                "user", Map.of("id", testUser.getId()),
                "book", Map.of("id", testBook.getId())
        );

        mockMvc.perform(post("/api/ratings")
                        .with(user(testUsername)).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRating)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.value", is(4)));
    }

    @Test
    void shouldDeleteRating() throws Exception {
        mockMvc.perform(delete("/api/ratings/" + testRating.getId())
                        .with(user(testUsername)).with(csrf()))
                .andExpect(status().isNoContent());
    }
}