package com.hugobumba.cinemasystem.reservation;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class ReservationSeatId implements Serializable {
    private Long reservationId;

    private Long seatId;

    private Long showtimeId;
}
