package com.hugobumba.cinemasystem.reservation.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ReservationCreationRequestDTO {
    @NotNull(message = "Showtime id is required")
    private Long showtimeId;

    @NotEmpty(message = "At least one seat is required")
    private List<@NotNull(message = "Seat id is required") Long> seatIds;
}
