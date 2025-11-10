package cloudlibrary.example.demo.controller;

import cloudlibrary.example.demo.model.*;
import cloudlibrary.example.demo.repository.*;
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
public class CommentControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private User testUser;
    private Post testPost;
    private Comment testComment;
    private final String testUsername = "testuser";

    @BeforeEach
    void setUp() {
        commentRepository.deleteAll();
        postRepository.deleteAll();
        bookRepository.deleteAll();
        userRepository.deleteAll();

        testUser = new User();
        testUser.setUsername(testUsername);
        testUser.setEmail("test@example.com");
        testUser.setPassword(passwordEncoder.encode("password"));
        userRepository.save(testUser);

        Book book = new Book();
        book.setTitle("Libro para Posts");
        bookRepository.save(book);

        testPost = new Post();
        testPost.setTitle("Post de prueba");
        testPost.setContent("Contenido...");
        testPost.setAuthor(testUser);
        testPost.setBook(book);
        postRepository.save(testPost);

        testComment = new Comment();
        testComment.setContent("Mi primer comentario");
        testComment.setAuthor(testUser);
        testComment.setPost(testPost);
        commentRepository.save(testComment);
    }

    @Test
    void shouldGetCommentsByPost() throws Exception {
        mockMvc.perform(get("/api/comments/post/" + testPost.getId())
                        .with(user(testUsername)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].content", is("Mi primer comentario")));
    }

    @Test
    void shouldAddComment() throws Exception {
        Map<String, Object> newComment = Map.of(
                "content", "Nuevo comentario",
                "author", Map.of("id", testUser.getId()),
                "post", Map.of("id", testPost.getId())
        );

        mockMvc.perform(post("/api/comments")
                        .with(user(testUsername)).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newComment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.content", is("Nuevo comentario")));
    }

    @Test
    void shouldDeleteComment() throws Exception {
        mockMvc.perform(delete("/api/comments/" + testComment.getId())
                        .with(user(testUsername)).with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldAddReplyToComment() throws Exception {
        Map<String, Object> reply = Map.of(
                "content", "Esta es una respuesta",
                "author", Map.of("id", testUser.getId())
        );

        mockMvc.perform(post("/api/comments/" + testComment.getId() + "/reply")
                        .with(user(testUsername)).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reply)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.content", is("Esta es una respuesta")))
                .andExpect(jsonPath("$.parent.id", is(testComment.getId().intValue())));
    }

    @Test
    void shouldReturnNotFoundWhenReplyingToInvalidParent() throws Exception {
        Map<String, Object> reply = Map.of(
                "content", "Respuesta fallida",
                "author", Map.of("id", testUser.getId())
        );

        mockMvc.perform(post("/api/comments/9999/reply")
                        .with(user(testUsername)).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reply)))
                .andExpect(status().isNotFound());
    }
}