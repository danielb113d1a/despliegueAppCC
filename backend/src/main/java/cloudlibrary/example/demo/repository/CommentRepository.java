package cloudlibrary.example.demo.repository;

import cloudlibrary.example.demo.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
 // Buscar comentarios por ID del post
    List<Comment> findByPostId(Long postId);
}
