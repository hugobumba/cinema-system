package com.hugobumba.cinemasystem.reservation.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReservationCreationRequestDTO {
    private Long userId;
    private Long showtimeId;
    private java.util.List<Long> seatIds;
}
