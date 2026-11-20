package com.hugobumba.cinemasystem.seat.dtos;

import com.hugobumba.cinemasystem.seat.SeatRow;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SeatRequestDTO {
    @NotNull(message = "Room id is required")
    private Long roomId;

    @NotNull(message = "Seat row is required")
    private SeatRow seatRow;

    @NotBlank(message = "Seat number is required")
    private String seatNumber;
}
