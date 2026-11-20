package com.hugobumba.cinemasystem.movie;

import com.hugobumba.cinemasystem.showtime.Showtime;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "movie")
public class Movie {
    @EqualsAndHashCode.Include
    @Id
    private Long id;

    private String title;

    private String description;

    private String posterUrl;

    @Enumerated(EnumType.STRING)
    private MovieGenre genre;

    private Integer durationInMinutes;

    @Enumerated(EnumType.STRING)
    private AgeRating ageRating;

    @OneToMany(mappedBy = "movie")
    private Set<Showtime> showtimes = new HashSet<>();
}
