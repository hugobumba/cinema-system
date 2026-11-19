package com.hugobumba.cinemasystem.reservation;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
public class ReservationSeat {
    @EmbeddedId
    private ReservationSeatId id;
}
