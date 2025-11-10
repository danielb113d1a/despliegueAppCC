package cloudlibrary.example.demo.controller;

import cloudlibrary.example.demo.model.Comment;
import cloudlibrary.example.demo.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private static final Logger log = LoggerFactory.getLogger(CommentController.class);

    private final CommentService commentService;
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<Comment>> getCommentsByPost(@PathVariable Long postId) {
        log.info("Request GET /api/comments/post/{} - Solicitando comentarios para el post", postId);
        return ResponseEntity.ok(commentService.getCommentsByPost(postId));
    }

    @PostMapping
    public ResponseEntity<Comment> addComment(@RequestBody Comment comment) {
        log.info("Request POST /api/comments - Añadiendo nuevo comentario para el post {}", comment.getPost().getId());
        return ResponseEntity.ok(commentService.addComment(comment));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        log.info("Request DELETE /api/comments/{} - Eliminando comentario", id);
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{parentId}/reply")
    public ResponseEntity<Comment> addReplyToComment(
            @PathVariable Long parentId,
            @RequestBody Comment reply) {

        log.info("Request POST /api/comments/{}/reply - Añadiendo respuesta al comentario", parentId);

        try {
            Comment savedReply = commentService.addReply(parentId, reply);
            return ResponseEntity.ok(savedReply);
        } catch (IllegalArgumentException e) {
            log.warn("No se pudo añadir la respuesta. Comentario padre con ID: {} no encontrado.", parentId);
            return ResponseEntity.notFound().build(); // Si no encuentra el padre
        }
    }
}