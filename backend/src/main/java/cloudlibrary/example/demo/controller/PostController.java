package cloudlibrary.example.demo.controller;

import cloudlibrary.example.demo.model.Post;
import cloudlibrary.example.demo.model.User;
import cloudlibrary.example.demo.service.PostService;
import cloudlibrary.example.demo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private static final Logger log = LoggerFactory.getLogger(PostController.class);

    private final PostService postService;
    private final UserService userService;

    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        log.info("Request GET /api/posts - Solicitando todos los posts");
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        log.info("Request GET /api/posts/{} - Solicitando post por ID", id);

        return postService.getPostById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("No se encontró el post con ID: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @PostMapping
    public ResponseEntity<?> addPost(@RequestBody Map<String, String> postData) {
        String title = postData.get("title");
        String content = postData.get("content");
        String email = postData.get("email");

        log.info("Request POST /api/posts - Usuario [{}] creando post: {}", email, title);

        try {
            User userEncontrado = userService.getUserByEmail(email);

            Post newPost = new Post();
            newPost.setTitle(title);
            newPost.setContent(content);
            newPost.setAuthor(userEncontrado);

            return ResponseEntity.ok(postService.addPost(newPost));

        } catch (Exception e) {
            log.error("Error creando post: ", e);
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        log.info("Request DELETE /api/posts/{} - Eliminando post", id);
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}