package com.hugobumba.cinemasystem.showtime.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
public class ShowtimeRequestDTO {
    private Long id;

    @NotNull(message = "Room id is required")
    private Long roomId;

    @NotNull(message = "Movie id is required")
    private Long movieId;

    @NotNull(message = "Showtime start time is required")
    private LocalDateTime startTime;

    @NotNull(message = "Showtime price is required")
    @DecimalMin(value = "0.01", message = "Showtime price must be greater than zero")
    private BigDecimal price;
}
