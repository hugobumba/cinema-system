package com.hugobumba.cinemasystem.room;

import com.hugobumba.cinemasystem.cinema.Cinema;
import com.hugobumba.cinemasystem.seat.Seat;
import com.hugobumba.cinemasystem.showtime.Showtime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Table(name = "room")
public class Room {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String number;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cinema_id", nullable = false)
    private Cinema cinema;

    @OneToMany(mappedBy = "room")
    private Set<Seat> seats = new HashSet<>();

    @OneToMany(mappedBy = "room")
    private Set<Showtime> showtimes = new HashSet<>();
}
