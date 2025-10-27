package cloudlibrary.example.demo.service;

import cloudlibrary.example.demo.model.Rating;
import cloudlibrary.example.demo.repository.RatingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService {
    private final RatingRepository ratingRepository;

    public RatingService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    public Rating save(Rating rating) {
        return ratingRepository.save(rating);
    }

    public Double averageRatingForBook(Long bookId) {
        return ratingRepository.findAverageByBook(bookId);
    }

    public List<Rating> getRatingsByBook(Long bookId) {
        return ratingRepository.findByBookId(bookId);
    }

    public Rating addRating(Rating rating) {
        if (rating.getBook() == null) {
            throw new IllegalArgumentException("La valoración debe pertenecer a un libro");
        }
        return ratingRepository.save(rating);
    }

    public void deleteRating(Long ratingId) {
        if (!ratingRepository.existsById(ratingId)) {
            throw new IllegalArgumentException("Rating no encontrado");
        }
        ratingRepository.deleteById(ratingId);
    }
}
