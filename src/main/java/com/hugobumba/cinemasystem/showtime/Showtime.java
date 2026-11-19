package com.hugobumba.cinemasystem.showtime;

import com.hugobumba.cinemasystem.movie.Movie;
import com.hugobumba.cinemasystem.reservation.Reservation;
import com.hugobumba.cinemasystem.reservation.ReservationSeat;
import com.hugobumba.cinemasystem.room.Room;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Showtime {
    @EqualsAndHashCode.Include
    @Id
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @ManyToOne(optional = false)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    private LocalDateTime startTime;

    private Double price;

    @OneToMany(mappedBy = "showtime")
    private Set<Reservation> reservations = new HashSet<>();

    @OneToMany(mappedBy = "showtime")
    private Set<ReservationSeat> reservationSeats = new HashSet<>();
}
