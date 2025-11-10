package cloudlibrary.example.demo.controller;

import cloudlibrary.example.demo.model.User;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

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
public class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private User testUser;
    private final String testUsername = "testuser";
    private final String testPassword = "password123";

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        testUser = new User();
        testUser.setUsername(testUsername);
        testUser.setEmail("test@example.com");
        testUser.setPassword(passwordEncoder.encode(testPassword));
        userRepository.save(testUser);
    }

    @Test
    void shouldRegisterNewUser() throws Exception {
        Map<String, String> newUser = Map.of(
                "username", "nuevo",
                "email", "nuevo@example.com",
                "password", "newpass"
        );

        mockMvc.perform(post("/api/users/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.username", is("nuevo")))
                .andExpect(jsonPath("$.password", not(is("newpass"))));
    }

    @Test
    void shouldNotRegisterDuplicateEmail() throws Exception {
        Map<String, String> duplicateUser = Map.of(
                "username", "otro",
                "email", "test@example.com",
                "password", "pass"
        );

        mockMvc.perform(post("/api/users/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicateUser)))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldAuthenticateUser() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("email", testUser.getEmail());
        params.add("password", testPassword);

        mockMvc.perform(post("/api/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .params(params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(true)));
    }

    @Test
    void shouldFailAuthenticationWithWrongPassword() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("email", testUser.getEmail());
        params.add("password", "wrongpass");

        mockMvc.perform(post("/api/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .params(params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(false)));
    }

    @Test
    void shouldGetProfile() throws Exception {
        mockMvc.perform(get("/api/users/" + testUser.getId())
                        .with(user(testUsername)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testUser.getId().intValue())))
                .andExpect(jsonPath("$.username", is(testUsername)));
    }
}