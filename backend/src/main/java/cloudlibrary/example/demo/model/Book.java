package cloudlibrary.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "books")
@Getter
@Setter
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User user;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Rating> ratings;

    public Double getAverageRating() {
        if (ratings == null || ratings.isEmpty()) return 0.0;
        double sum = 0;
        for (Rating r : ratings) sum += r.getValue();
        return sum / ratings.size();
    }

    public Integer getTotalRatings() {
        return ratings == null ? 0 : ratings.size();
    }
}