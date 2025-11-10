package cloudlibrary.example.demo.controller;

import cloudlibrary.example.demo.model.Post;
import cloudlibrary.example.demo.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private static final Logger log = LoggerFactory.getLogger(PostController.class);

    private final PostService postService;
    public PostController(PostService postService) {
        this.postService = postService;
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
    public ResponseEntity<Post> addPost(@RequestBody Post post) {
        log.info("Request POST /api/posts - Creando nuevo post con título: {}", post.getTitle());
        return ResponseEntity.ok(postService.addPost(post));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        log.info("Request DELETE /api/posts/{} - Eliminando post", id);
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}