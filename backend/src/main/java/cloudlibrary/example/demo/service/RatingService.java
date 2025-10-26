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

    // Obtener todas las valoraciones de un libro
    public List<Rating> getRatingsByBook(Long bookId) {
        return ratingRepository.findByBookId(bookId);
    }

    // Añadir una valoración
    public Rating addRating(Rating rating) {
        if (rating.getBook() == null) {
            throw new IllegalArgumentException("La valoración debe pertenecer a un libro");
        }
        return ratingRepository.save(rating);
    }

    // Eliminar una valoración
    public void deleteRating(Long ratingId) {
        if (!ratingRepository.existsById(ratingId)) {
            throw new IllegalArgumentException("Rating no encontrado");
        }
        ratingRepository.deleteById(ratingId);
    }
}
