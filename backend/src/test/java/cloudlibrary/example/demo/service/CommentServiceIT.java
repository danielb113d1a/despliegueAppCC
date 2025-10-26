package cloudlibrary.example.demo.service;

import cloudlibrary.example.demo.model.Book;
import cloudlibrary.example.demo.model.Comment;
import cloudlibrary.example.demo.model.Post;
import cloudlibrary.example.demo.model.User;
import cloudlibrary.example.demo.repository.BookRepository;
import cloudlibrary.example.demo.repository.CommentRepository;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@Transactional
class CommentServiceIT {

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

    @Autowired private CommentService commentService;
    @Autowired private UserService userService;
    @Autowired private PostService postService;

    @Autowired private CommentRepository commentRepository;
    @Autowired private PostRepository postRepository;
    @Autowired private BookRepository bookRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    private User testUser;
    private Post testPost;

    @BeforeEach
    void setup() {
        commentRepository.deleteAll();
        postRepository.deleteAll();
        bookRepository.deleteAll();
        userRepository.deleteAll();

        // Creamos un usuario
        User user = new User();
        user.setUsername("Comentador");
        user.setEmail("user" + System.currentTimeMillis() + "@example.com");
        user.setPassword("123");
        testUser = userService.registerUser(user);

        // Creamos un Post (con su libro)
        Book book = new Book();
        book.setTitle("Libro de prueba");
        book.setAuthor("Autor");

        Post post = new Post();
        post.setTitle("Post de prueba");
        post.setAuthor(testUser);
        post.setBook(book);
        testPost = postService.addPost(post); // addPost se encarga del Cascade
    }

    @Test
    void shouldAddReplyToComment() {
        // 1. Creamos un comentario padre
        Comment parentComment = new Comment();
        parentComment.setContent("Este es el comentario padre");
        parentComment.setAuthor(testUser);
        parentComment.setPost(testPost);
        Comment savedParent = commentService.addComment(parentComment);

        // 2. Creamos una respuesta
        Comment reply = new Comment();
        reply.setContent("Esta es la respuesta");
        reply.setAuthor(testUser);
        // OJO: No seteamos el Post aquí, el servicio 'addReply' debe hacerlo

        // 3. Llamamos al servicio de 'addReply'
        Comment savedReply = commentService.addReply(savedParent.getId(), reply);

        // 4. Verificamos
        assertThat(savedReply.getId()).isNotNull();
        assertThat(savedReply.getContent()).isEqualTo("Esta es la respuesta");

        // Verificamos la relación
        assertThat(savedReply.getParent()).isNotNull();
        assertThat(savedReply.getParent().getId()).isEqualTo(savedParent.getId());

        // Verificamos que la respuesta se asignó al mismo post que el padre
        assertThat(savedReply.getPost().getId()).isEqualTo(testPost.getId());
    }

    @Test
    void shouldGetCommentsByPost() {
        Comment c1 = new Comment();
        c1.setContent("C1");
        c1.setAuthor(testUser);
        c1.setPost(testPost);
        commentService.addComment(c1);

        Comment c2 = new Comment();
        c2.setContent("C2");
        c2.setAuthor(testUser);
        c2.setPost(testPost);
        commentService.addComment(c2);

        List<Comment> comments = commentService.getCommentsByPost(testPost.getId());

        assertThat(comments).hasSize(2);
        assertThat(comments).extracting(Comment::getContent).containsExactlyInAnyOrder("C1", "C2");
    }
}