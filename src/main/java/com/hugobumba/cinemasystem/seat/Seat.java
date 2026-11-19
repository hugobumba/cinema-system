package com.hugobumba.cinemasystem.seat;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
public class Seat {
    @Id
    private Long id;

    private Long roomId;

    private SeatRow seatRow;

    private String seatNumber;
}
