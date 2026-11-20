package com.hugobumba.cinemasystem.seat;

import com.hugobumba.cinemasystem.reservation.ReservationSeat;
import com.hugobumba.cinemasystem.room.Room;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "seat")
public class Seat {
    @EqualsAndHashCode.Include
    @Id
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Enumerated(EnumType.STRING)
    private SeatRow seatRow;

    private String seatNumber;

    @OneToMany(mappedBy = "seat")
    private Set<ReservationSeat> reservationSeats = new HashSet<>();
}
