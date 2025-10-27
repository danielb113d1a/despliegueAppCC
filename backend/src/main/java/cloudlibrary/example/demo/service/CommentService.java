package cloudlibrary.example.demo.service;

import cloudlibrary.example.demo.model.Book;
import cloudlibrary.example.demo.model.Comment;
import cloudlibrary.example.demo.repository.BookRepository;
import cloudlibrary.example.demo.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final BookRepository bookRepository;

    public CommentService(CommentRepository commentRepository, BookRepository bookRepository) {
        this.commentRepository = commentRepository;
        this.bookRepository = bookRepository;
    }

    public List<Comment> findByPost(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    public List<Comment> getCommentsByPost(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    public Comment addComment(Comment comment) {
        if (comment.getPost() == null || comment.getAuthor() == null) {
            throw new IllegalArgumentException("El comentario debe tener post y autor");
        }
        return commentRepository.save(comment);
    }

    public void deleteComment(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new IllegalArgumentException("Comentario no encontrado");
        }
        commentRepository.deleteById(commentId);
    }

    public Comment addReply(Long parentId, Comment reply) {

        Comment parent = commentRepository.findById(parentId)
                .orElseThrow(() -> new IllegalArgumentException("Comentario padre no encontrado"));

        reply.setParent(parent); // ¡La clave está aquí!
        reply.setPost(parent.getPost()); // La respuesta pertenece al mismo post que el padre

        if (reply.getAuthor() == null) {
            throw new IllegalArgumentException("La respuesta debe tener un autor");
        }

        return commentRepository.save(reply);
    }
}

