package com.hugobumba.cinemasystem.reservation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface ReservationSeatRepository extends JpaRepository<ReservationSeat, ReservationSeatId> {
    long countByIdShowtimeIdAndIdSeatIdIn(Long showtimeId, Collection<Long> seatIds);

    boolean existsByIdShowtimeIdAndSeatId(Long showtimeId, Long seatId);

    void deleteByReservationId(Long reservationId);

    void deleteByIdShowtimeIdAndSeatId(Long showtimeId, Long seatId);
}
