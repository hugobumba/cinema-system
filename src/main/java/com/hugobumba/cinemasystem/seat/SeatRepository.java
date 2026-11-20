package com.hugobumba.cinemasystem.seat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByRoomId(Long roomId);

    Optional<Seat> findByRoomIdAndSeatRowAndSeatNumber(Long roomId, SeatRow seatRow, String seatNumber);

    //TODO: I DONT LIKE IT SHOULD FIND A BETTER WAY TO DO THIS
    @Query("""
            SELECT s
            FROM Seat s
            WHERE s.room.id = (SELECT st.room.id FROM Showtime st WHERE st.id = :showtimeId)
              AND NOT EXISTS (
                SELECT 1
                FROM ReservationSeat rs
                WHERE rs.id.showtimeId = :showtimeId
                  AND rs.seat.id = s.id
              )
            """)
    List<Seat> findAvailableSeatsByShowtimeId(@Param("showtimeId") Long showtimeId);
}
