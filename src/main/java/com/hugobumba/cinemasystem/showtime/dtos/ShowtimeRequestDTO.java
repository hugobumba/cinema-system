package com.hugobumba.cinemasystem.showtime.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
public class ShowtimeRequestDTO {
    private Long id;
    private Long roomId;
    private Long movieId;
    private LocalDateTime startTime;
    private Double price;
}
