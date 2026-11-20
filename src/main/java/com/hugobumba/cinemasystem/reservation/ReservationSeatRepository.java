package com.hugobumba.cinemasystem.reservation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface ReservationSeatRepository extends JpaRepository<ReservationSeat, ReservationSeatId> {
    long countByIdShowtimeIdAndIdSeatIdIn(Long showtimeId, Collection<Long> seatIds);

    void deleteByReservationId(Long reservationId);
}
