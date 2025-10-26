package cloudlibrary.example.demo.controller;

import cloudlibrary.example.demo.model.Post;
import cloudlibrary.example.demo.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostControllerTest {

    @Mock
    private PostService postService;

    @InjectMocks
    private PostController postController;

    private Post testPost;

    @BeforeEach
    void setUp() {
        testPost = new Post();
        testPost.setId(1L);
        testPost.setTitle("Mi reseña");
    }

    @Test
    void shouldGetAllPosts() {
        when(postService.getAllPosts()).thenReturn(List.of(testPost));

        ResponseEntity<List<Post>> response = postController.getAllPosts();

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).hasSize(1);
        verify(postService, times(1)).getAllPosts();
    }

    @Test
    void shouldGetPostById() {
        when(postService.getPostById(1L)).thenReturn(Optional.of(testPost));

        ResponseEntity<Post> response = postController.getPostById(1L);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody().getTitle()).isEqualTo("Mi reseña");
        verify(postService, times(1)).getPostById(1L);
    }

    @Test
    void shouldReturnNotFoundForInvalidPostId() {
        when(postService.getPostById(99L)).thenReturn(Optional.empty());

        ResponseEntity<Post> response = postController.getPostById(99L);

        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
    }

    @Test
    void shouldAddPost() {
        when(postService.addPost(any(Post.class))).thenReturn(testPost);

        ResponseEntity<Post> response = postController.addPost(testPost);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isEqualTo(testPost);
        verify(postService, times(1)).addPost(any(Post.class));
    }

    @Test
    void shouldDeletePost() {
        doNothing().when(postService).deletePost(1L);

        ResponseEntity<Void> response = postController.deletePost(1L);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        verify(postService, times(1)).deletePost(1L);
    }
}