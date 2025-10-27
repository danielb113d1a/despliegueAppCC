package cloudlibrary.example.demo.service;

import cloudlibrary.example.demo.model.Post;
import cloudlibrary.example.demo.repository.BookRepository;
import cloudlibrary.example.demo.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final BookRepository bookRepository;

    public PostService(PostRepository postRepository, BookRepository bookRepository) {
        this.postRepository = postRepository;
        this.bookRepository = bookRepository;
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public Post save(Post post) {
        return postRepository.save(post);
    }

    public List<Post> findByBookId(Long bookId) {
        return postRepository.findByBookId(bookId);
    }

    public List<Post> findByUserId(Long userId) {
        return postRepository.findByAuthorId(userId);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }


    public Post addPost(Post post) {
        if (post.getAuthor() == null) {
            throw new IllegalArgumentException("El post debe tener un usuario");
        }

        if (post.getBook() != null && post.getBook().getId() == null) {
            bookRepository.save(post.getBook());
        }

        return postRepository.save(post);
    }

    public void deletePost(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new IllegalArgumentException("Post no encontrado");
        }
        postRepository.deleteById(postId);
    }
}

