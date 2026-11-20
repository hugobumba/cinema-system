package com.hugobumba.cinemasystem.reservation;

import com.hugobumba.cinemasystem.seat.Seat;
import com.hugobumba.cinemasystem.showtime.Showtime;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "reservation_seat")
public class ReservationSeat {
    @EqualsAndHashCode.Include
    @EmbeddedId
    private ReservationSeatId id;

    @MapsId("reservationId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @MapsId("seatId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    @MapsId("showtimeId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "showtime_id", nullable = false)
    private Showtime showtime;
}
