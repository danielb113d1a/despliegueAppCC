package cloudlibrary.example.demo.repository;

import cloudlibrary.example.demo.model.Book;
import cloudlibrary.example.demo.model.Rating;
import cloudlibrary.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByBookId(Long bookId);

    @Query("SELECT AVG(r.value) FROM Rating r WHERE r.book.id = :bookId")
    Double findAverageByBook(@Param("bookId") Long bookId);

    Optional<Rating> findByUserAndBook(User user, Book book);

    List<Rating> findByUser_Email(String email);
}
