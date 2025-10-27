package cloudlibrary.example.demo.service;

import cloudlibrary.example.demo.model.Book;
import cloudlibrary.example.demo.model.Post;
import cloudlibrary.example.demo.model.User;
import cloudlibrary.example.demo.repository.BookRepository;
import cloudlibrary.example.demo.repository.PostRepository;
import cloudlibrary.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@Transactional
class PostServiceIT {

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

    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private User testUser;

    @BeforeEach
    void setup() {
        postRepository.deleteAll();
        bookRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User();
        user.setUsername("Autor");
        user.setEmail("autor" + System.currentTimeMillis() + "@example.com");
        user.setPassword("123");

        testUser = userService.registerUser(user);
    }

    @Test
    void shouldSavePostAndNewBookWithCascade() {
        Book newBook = new Book();
        newBook.setTitle("Dune");
        newBook.setAuthor("Frank Herbert");

        Post post = new Post();
        post.setTitle("Mi reseña de Dune");
        post.setContent("Increíble...");

        post.setAuthor(testUser);
        post.setBook(newBook);

        Post savedPost = postService.addPost(post);

        assertThat(savedPost.getId()).isNotNull();
        assertThat(savedPost.getAuthor().getId()).isEqualTo(testUser.getId());
        assertThat(savedPost.getBook().getId()).isNotNull();

        assertThat(postRepository.count()).isEqualTo(1);
        assertThat(bookRepository.count()).isEqualTo(1);

        Book savedBook = bookRepository.findAll().get(0);
        assertThat(savedBook.getTitle()).isEqualTo("Dune");
    }
}