package com.hugobumba.cinemasystem.seat.dtos;

import com.hugobumba.cinemasystem.seat.SeatRow;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SeatRequestDTO {
    private Long id;
    private Long roomId;
    private SeatRow seatRow;
    private String seatNumber;
}
