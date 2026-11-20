package com.hugobumba.cinemasystem.movie.dtos;

import com.hugobumba.cinemasystem.movie.AgeRating;
import com.hugobumba.cinemasystem.movie.MovieGenre;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MovieRequestDTO {
    private Long id;

    @NotBlank(message = "Movie title is required")
    private String title;

    @NotBlank(message = "Movie description is required")
    private String description;

    private String posterUrl;

    @NotNull(message = "Movie genre is required")
    private MovieGenre genre;

    @NotNull(message = "Movie duration is required")
    private Integer durationInMinutes;

    @NotNull(message = "Movie age rating is required")
    private AgeRating ageRating;
}
