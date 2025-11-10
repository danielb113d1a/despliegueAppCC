package cloudlibrary.example.demo.controller;

import cloudlibrary.example.demo.model.Book;
import cloudlibrary.example.demo.model.Post;
import cloudlibrary.example.demo.model.User;
import cloudlibrary.example.demo.repository.BookRepository;
import cloudlibrary.example.demo.repository.PostRepository;
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
public class PostControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private User testUser;
    private Book testBook;
    private Post testPost;
    private final String testUsername = "testuser";

    @BeforeEach
    void setUp() {
        postRepository.deleteAll();
        bookRepository.deleteAll();
        userRepository.deleteAll();

        testUser = new User();
        testUser.setUsername(testUsername);
        testUser.setEmail("test@example.com");
        testUser.setPassword(passwordEncoder.encode("password"));
        userRepository.save(testUser);

        testBook = new Book();
        testBook.setTitle("Libro para Posts");
        bookRepository.save(testBook);

        testPost = new Post();
        testPost.setTitle("Post de prueba");
        testPost.setContent("Contenido...");
        testPost.setAuthor(testUser);
        testPost.setBook(testBook);
        postRepository.save(testPost);
    }

    @Test
    void shouldGetAllPosts() throws Exception {
        mockMvc.perform(get("/api/posts")
                        .with(user(testUsername)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("Post de prueba")));
    }

    @Test
    void shouldGetPostById() throws Exception {
        mockMvc.perform(get("/api/posts/" + testPost.getId())
                        .with(user(testUsername)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(testPost.getTitle())));
    }

    @Test
    void shouldReturnNotFoundForInvalidPostId() throws Exception {
        mockMvc.perform(get("/api/posts/9999")
                        .with(user(testUsername)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeletePost() throws Exception {
        mockMvc.perform(delete("/api/posts/" + testPost.getId())
                        .with(user(testUsername)).with(csrf()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/posts/" + testPost.getId())
                        .with(user(testUsername)))
                .andExpect(status().isNotFound());
    }
}