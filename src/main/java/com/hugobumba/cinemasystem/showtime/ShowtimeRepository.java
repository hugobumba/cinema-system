package com.hugobumba.cinemasystem.showtime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {
    List<Showtime> findByMovieId(Long movieId);

    List<Showtime> findByRoomId(Long roomId);

    boolean existsByRoomIdAndStartTime(Long roomId, java.time.LocalDateTime startTime);

    boolean existsByRoomIdAndStartTimeAndIdNot(Long roomId, java.time.LocalDateTime startTime, Long id);
}
