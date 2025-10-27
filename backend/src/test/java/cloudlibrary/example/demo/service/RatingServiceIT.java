package cloudlibrary.example.demo.service;

import cloudlibrary.example.demo.model.Book;
import cloudlibrary.example.demo.model.Rating;
import cloudlibrary.example.demo.model.User;
import cloudlibrary.example.demo.repository.BookRepository;
import cloudlibrary.example.demo.repository.RatingRepository;
import cloudlibrary.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
class RatingServiceIT {

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

    @Autowired private RatingService ratingService;
    @Autowired private UserService userService;

    @Autowired private RatingRepository ratingRepository;
    @Autowired private BookRepository bookRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    private User testUser;
    private Book testBook;

    @BeforeEach
    void setup() {
        ratingRepository.deleteAll();
        bookRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User();
        user.setUsername("Rater");
        user.setEmail("user" + System.currentTimeMillis() + "@example.com");
        user.setPassword("123");
        testUser = userService.registerUser(user);

        Book book = new Book();
        book.setTitle("Libro para valorar");
        book.setAuthor("Autor");
        testBook = bookRepository.save(book);
    }

    @Test
    void shouldCalculateAverageRatingForBook() {
        Rating r1 = new Rating();
        r1.setBook(testBook);
        r1.setUser(testUser);
        r1.setValue(5);
        ratingService.addRating(r1);

        Rating r2 = new Rating();
        r2.setBook(testBook);
        r2.setUser(testUser);
        r2.setValue(4);
        ratingService.addRating(r2);

        Rating r3 = new Rating();
        r3.setBook(testBook);
        r3.setUser(testUser);
        r3.setValue(3);
        ratingService.addRating(r3);

        Double average = ratingService.averageRatingForBook(testBook.getId());

        assertThat(average).isEqualTo(4.0);
    }

    @Test
    void shouldReturnNullAverageForBookWithNoRatings() {
        Double average = ratingService.averageRatingForBook(testBook.getId());

        assertThat(average).isNull();
    }
}