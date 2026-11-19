package com.hugobumba.cinemasystem.showtime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
public class Showtime {
    @Id
    private Long id;

    private Long roomId;

    private Long movieId;

    private LocalDateTime startTime;

    private Double price;
}
