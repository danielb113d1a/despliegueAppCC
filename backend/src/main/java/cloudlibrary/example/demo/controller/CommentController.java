package cloudlibrary.example.demo.controller;

import cloudlibrary.example.demo.model.Comment;
import cloudlibrary.example.demo.model.Post;
import cloudlibrary.example.demo.model.User;
import cloudlibrary.example.demo.service.CommentService;
import cloudlibrary.example.demo.service.PostService;
import cloudlibrary.example.demo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private static final Logger log = LoggerFactory.getLogger(CommentController.class);

    private final CommentService commentService;
    private final UserService userService;
    private final PostService postService;

    public CommentController(CommentService commentService, UserService userService, PostService postService) {
        this.commentService = commentService;
        this.userService = userService;
        this.postService = postService;
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<Comment>> getCommentsByPost(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentsByPost(postId));
    }

    @PostMapping
    public ResponseEntity<?> addComment(@RequestBody Map<String, Object> commentData) {
        try {
            String content = (String) commentData.get("content");
            String email = (String) commentData.get("email");
            Long postId = Long.valueOf(commentData.get("postId").toString());

            log.info("Usuario {} comentando en post {}", email, postId);

            User author = userService.getUserByEmail(email);
            Post post = postService.getPostById(postId)
                    .orElseThrow(() -> new RuntimeException("Post no encontrado"));

            Comment newComment = new Comment();
            newComment.setContent(content);
            newComment.setAuthor(author);
            newComment.setPost(post);

            return ResponseEntity.ok(commentService.addComment(newComment));

        } catch (Exception e) {
            log.error("Error al comentar", e);
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{parentId}/reply")
    public ResponseEntity<?> addReplyToComment(
            @PathVariable Long parentId,
            @RequestBody Map<String, Object> replyData) {

        try {
            String content = (String) replyData.get("content");
            String email = (String) replyData.get("email");

            log.info("Usuario {} respondiendo al comentario {}", email, parentId);

            User author = userService.getUserByEmail(email);

            Comment reply = new Comment();
            reply.setContent(content);
            reply.setAuthor(author);

            return ResponseEntity.ok(commentService.addReply(parentId, reply));

        } catch (Exception e) {
            log.error("Error al responder comentario", e);
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}