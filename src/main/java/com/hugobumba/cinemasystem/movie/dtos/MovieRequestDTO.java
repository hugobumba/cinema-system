package com.hugobumba.cinemasystem.movie.dtos;

import com.hugobumba.cinemasystem.movie.AgeRating;
import com.hugobumba.cinemasystem.movie.MovieGenre;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MovieRequestDTO {
    private Long id;
    private String title;
    private String description;
    private String posterUrl;
    private MovieGenre genre;
    private Integer durationInMinutes;
    private AgeRating ageRating;
}
