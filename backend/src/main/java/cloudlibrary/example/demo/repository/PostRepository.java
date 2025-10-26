package cloudlibrary.example.demo.repository;

import cloudlibrary.example.demo.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByAuthorId(Long userId);
    List<Post> findByBookId(Long bookId);
}
