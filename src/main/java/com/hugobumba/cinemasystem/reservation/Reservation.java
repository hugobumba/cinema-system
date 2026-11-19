package com.hugobumba.cinemasystem.reservation;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
public class Reservation {
    @Id
    private Long id;

    private Long userId;

    private Long showtimeId;

    private ReservationStatus status;

    private Double totalPrice;

    private LocalDateTime createdAt;
}
