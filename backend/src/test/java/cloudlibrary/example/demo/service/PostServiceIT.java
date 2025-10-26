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
// Importa @Transactional
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@Transactional // <- Añade esto para manejar las sesiones entre setup y test
class PostServiceIT {

    // --- Configuración de Testcontainers ---
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

    // --- Dependencias ---
    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService; // Necesario para crear el autor

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder; // Requerido por UserService

    // --- ESTA ES LA VARIABLE CRÍTICA ---
    private User testUser;

    // --- ESTE ES EL MÉTODO CRÍTICO ---
    @BeforeEach
    void setup() {
        // Limpiamos todo
        postRepository.deleteAll();
        bookRepository.deleteAll();
        userRepository.deleteAll();

        // Creamos un usuario autor para los tests
        User user = new User();
        user.setUsername("Autor");
        user.setEmail("autor" + System.currentTimeMillis() + "@example.com");
        user.setPassword("123");

        // Aquí es donde 'testUser' DEBE ser inicializado
        testUser = userService.registerUser(user);
    }

    @Test
    void shouldSavePostAndNewBookWithCascade() {
        // 1. Preparamos el Libro nuevo
        Book newBook = new Book();
        newBook.setTitle("Dune");
        newBook.setAuthor("Frank Herbert");

        Post post = new Post();
        post.setTitle("Mi reseña de Dune");
        post.setContent("Increíble...");

        // 'testUser' ya no será null gracias al setup()
        post.setAuthor(testUser);
        post.setBook(newBook);

        // 2. Llamamos al servicio
        Post savedPost = postService.addPost(post);

        // 3. Verificamos
        assertThat(savedPost.getId()).isNotNull();
        assertThat(savedPost.getAuthor().getId()).isEqualTo(testUser.getId());
        assertThat(savedPost.getBook().getId()).isNotNull(); // El libro debe tener un ID

        // Verificamos que ambos existen en la BBDD
        assertThat(postRepository.count()).isEqualTo(1);
        assertThat(bookRepository.count()).isEqualTo(1);

        Book savedBook = bookRepository.findAll().get(0);
        assertThat(savedBook.getTitle()).isEqualTo("Dune");
    }
}