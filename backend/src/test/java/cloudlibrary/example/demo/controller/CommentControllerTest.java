package cloudlibrary.example.demo.controller;

import cloudlibrary.example.demo.model.Comment;
import cloudlibrary.example.demo.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentControllerTest {

    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentController commentController;

    private Comment testComment;

    @BeforeEach
    void setUp() {
        testComment = new Comment();
        testComment.setId(1L);
        testComment.setContent("Gran post!");
    }

    @Test
    void shouldGetCommentsByPost() {
        // Este es el endpoint corregido que discutimos
        when(commentService.getCommentsByPost(1L)).thenReturn(List.of(testComment));

        ResponseEntity<List<Comment>> response = commentController.getCommentsByPost(1L);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).hasSize(1);
        verify(commentService, times(1)).getCommentsByPost(1L);
    }

    @Test
    void shouldAddComment() {
        when(commentService.addComment(any(Comment.class))).thenReturn(testComment);

        ResponseEntity<Comment> response = commentController.addComment(testComment);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isEqualTo(testComment);
        verify(commentService, times(1)).addComment(any(Comment.class));
    }

    @Test
    void shouldAddReplyToComment() {
        // Este es el test para el nuevo endpoint de hilos
        Comment reply = new Comment();
        reply.setId(2L);
        reply.setContent("No estoy de acuerdo");

        when(commentService.addReply(eq(1L), any(Comment.class))).thenReturn(reply);

        ResponseEntity<Comment> response = commentController.addReplyToComment(1L, reply);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody().getId()).isEqualTo(2L);
        verify(commentService, times(1)).addReply(eq(1L), any(Comment.class));
    }

    @Test
    void shouldDeleteComment() {
        doNothing().when(commentService).deleteComment(1L);

        ResponseEntity<Void> response = commentController.deleteComment(1L);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        verify(commentService, times(1)).deleteComment(1L);
    }
}