package com.hugobumba.cinemasystem.reservation;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationSeatRepository extends JpaRepository<ReservationSeat, ReservationSeatId> {
}
