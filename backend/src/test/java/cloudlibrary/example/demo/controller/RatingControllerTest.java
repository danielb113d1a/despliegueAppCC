package cloudlibrary.example.demo.controller;

import cloudlibrary.example.demo.model.Rating;
import cloudlibrary.example.demo.service.RatingService;
import org.junit.jupiter.api.Assertions;
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
class RatingControllerTest {

    @Mock
    private RatingService ratingService;

    @InjectMocks
    private RatingController ratingController;

    private Rating testRating;

    @BeforeEach
    void setUp() {
        testRating = new Rating();
        testRating.setId(1L);
        testRating.setValue(5);
    }

    @Test
    void shouldGetRatingsByBook() {
        when(ratingService.getRatingsByBook(1L)).thenReturn(List.of(testRating));

        ResponseEntity<List<Rating>> response = ratingController.getRatingsByBook(1L);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).hasSize(1);
        verify(ratingService, times(1)).getRatingsByBook(1L);
    }

    @Test
    void shouldDeleteRating() {
        doNothing().when(ratingService).deleteRating(1L);

        ResponseEntity<Void> response = ratingController.deleteRating(1L);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        verify(ratingService, times(1)).deleteRating(1L);
    }
}