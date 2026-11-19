package com.hugobumba.cinemasystem.movie;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
public class Movie {
    @Id
    private Long id;

    private String title;

    private String description;

    private String posterUrl;

    private MovieGenre genre;

    private Integer durationInMinutes;

    private AgeRating ageRating;
}
