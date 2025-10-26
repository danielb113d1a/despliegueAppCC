package cloudlibrary.example.demo.controller;

import cloudlibrary.example.demo.model.Comment;
import cloudlibrary.example.demo.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // BIEN: Buscamos comentarios por el post al que pertenecen
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<Comment>> getCommentsByPost(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentsByPost(postId));
    }

    @PostMapping
    public ResponseEntity<Comment> addComment(@RequestBody Comment comment) {
        return ResponseEntity.ok(commentService.addComment(comment));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{parentId}/reply")
    public ResponseEntity<Comment> addReplyToComment(
            @PathVariable Long parentId,
            @RequestBody Comment reply) {

        try {
            // Asumimos que el JSON del 'reply' ya trae el 'author'
            // (cuando tengas JWT, el autor se sacará del token)
            Comment savedReply = commentService.addReply(parentId, reply);
            return ResponseEntity.ok(savedReply);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build(); // Si no encuentra el padre
        }
    }
}
