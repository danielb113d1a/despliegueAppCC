package cloudlibrary.example.demo.controller;

import cloudlibrary.example.demo.model.Category;
import cloudlibrary.example.demo.model.User;
import cloudlibrary.example.demo.repository.CategoryRepository;
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
public class CategoryControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private Category testCategory;
    private final String testUsername = "testuser";

    @BeforeEach
    void setUp() {
        categoryRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User();
        user.setUsername(testUsername);
        user.setEmail("test@example.com");
        user.setPassword(passwordEncoder.encode("password"));
        userRepository.save(user);

        Category category = new Category();
        category.setName("Ciencia Ficción");
        testCategory = categoryRepository.save(category);
    }

    @Test
    void shouldGetAllCategories() throws Exception {
        mockMvc.perform(get("/api/categories")
                        .with(user(testUsername)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Ciencia Ficción")));
    }

    @Test
    void shouldGetCategoryById() throws Exception {
        mockMvc.perform(get("/api/categories/" + testCategory.getId())
                        .with(user(testUsername)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(testCategory.getName())));
    }

    @Test
    void shouldReturnNotFoundForInvalidCategoryId() throws Exception {
        mockMvc.perform(get("/api/categories/9999")
                        .with(user(testUsername)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateCategory() throws Exception {
        Category newCategory = new Category();
        newCategory.setName("Aventura");

        mockMvc.perform(post("/api/categories")
                        .with(user(testUsername)).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCategory)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Aventura")));
    }

    @Test
    void shouldUpdateCategory() throws Exception {
        Map<String, String> updatedName = Map.of("name", "Fantasía");

        mockMvc.perform(put("/api/categories/" + testCategory.getId())
                        .with(user(testUsername)).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedName)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testCategory.getId().intValue())))
                .andExpect(jsonPath("$.name", is("Fantasía")));
    }

    @Test
    void shouldDeleteCategory() throws Exception {
        mockMvc.perform(delete("/api/categories/" + testCategory.getId())
                        .with(user(testUsername)).with(csrf()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/categories/" + testCategory.getId())
                        .with(user(testUsername)))
                .andExpect(status().isNotFound());
    }
}