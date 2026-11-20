package com.hugobumba.cinemasystem.showtime;

import com.hugobumba.cinemasystem.movie.Movie;
import com.hugobumba.cinemasystem.movie.MovieRepository;
import com.hugobumba.cinemasystem.reservation.ReservationStatus;
import com.hugobumba.cinemasystem.room.Room;
import com.hugobumba.cinemasystem.room.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
public class ShowtimeService {

    private final ShowtimeRepository showtimeRepository;
    private final RoomRepository roomRepository;
    private final MovieRepository movieRepository;

    public ShowtimeService(
            ShowtimeRepository showtimeRepository,
            RoomRepository roomRepository,
            MovieRepository movieRepository
    ) {
        this.showtimeRepository = showtimeRepository;
        this.roomRepository = roomRepository;
        this.movieRepository = movieRepository;
    }

    @Transactional
    public Showtime createShowtime(Showtime showtime) {
        if (showtime == null) {
            throw new IllegalArgumentException("Showtime is required");
        }
        if (showtime.getRoom() == null || showtime.getRoom().getId() == null) {
            throw new IllegalArgumentException("Room is required");
        }
        if (showtime.getMovie() == null || showtime.getMovie().getId() == null) {
            throw new IllegalArgumentException("Movie is required");
        }
        if (showtime.getStartTime() == null) {
            throw new IllegalArgumentException("Showtime start time is required");
        }
        if (showtime.getPrice() == null || showtime.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Showtime price must be greater than zero");
        }
        Room room = roomRepository.findById(showtime.getRoom().getId())
                .orElseThrow(() -> new NoSuchElementException("Room not found: " + showtime.getRoom().getId()));
        Movie movie = movieRepository.findById(showtime.getMovie().getId())
                .orElseThrow(() -> new NoSuchElementException("Movie not found: " + showtime.getMovie().getId()));
        if (movie.getDurationInMinutes() == null || movie.getDurationInMinutes() <= 0) {
            throw new IllegalArgumentException("Movie duration must be greater than zero");
        }
        validateNoScheduleConflict(room.getId(), showtime.getStartTime(), movie.getDurationInMinutes(), null);

        showtime.setRoom(room);
        showtime.setMovie(movie);
        return showtimeRepository.save(showtime);
    }

    public Showtime getShowtimeById(Long id) {
        return showtimeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Showtime not found: " + id));
    }

    public List<Showtime> listAllShowtimes() {
        return showtimeRepository.findAll();
    }

    public List<Showtime> listShowtimesByMovie(Long movieId) {
        return showtimeRepository.findByMovieId(movieId);
    }

    public List<Showtime> listShowtimesByRoom(Long roomId) {
        return showtimeRepository.findByRoomId(roomId);
    }

    @Transactional
    public Showtime updateShowtime(Showtime showtime) {
        if (showtime == null || showtime.getId() == null) {
            throw new IllegalArgumentException("Showtime id is required");
        }
        Showtime existing = getShowtimeById(showtime.getId());
        if (showtime.getRoom() == null || showtime.getRoom().getId() == null) {
            throw new IllegalArgumentException("Room is required");
        }
        if (showtime.getMovie() == null || showtime.getMovie().getId() == null) {
            throw new IllegalArgumentException("Movie is required");
        }
        if (showtime.getStartTime() == null) {
            throw new IllegalArgumentException("Showtime start time is required");
        }
        if (showtime.getPrice() == null || showtime.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Showtime price must be greater than zero");
        }
        Room room = roomRepository.findById(showtime.getRoom().getId())
                .orElseThrow(() -> new NoSuchElementException("Room not found: " + showtime.getRoom().getId()));
        Movie movie = movieRepository.findById(showtime.getMovie().getId())
                .orElseThrow(() -> new NoSuchElementException("Movie not found: " + showtime.getMovie().getId()));
        if (movie.getDurationInMinutes() == null || movie.getDurationInMinutes() <= 0) {
            throw new IllegalArgumentException("Movie duration must be greater than zero");
        }
        validateNoScheduleConflict(room.getId(), showtime.getStartTime(), movie.getDurationInMinutes(), showtime.getId());

        existing.setRoom(room);
        existing.setMovie(movie);
        existing.setStartTime(showtime.getStartTime());
        existing.setPrice(showtime.getPrice());
        return showtimeRepository.save(existing);
    }

    @Transactional
    public void deleteShowtime(Long id) {
        Showtime showtime = getShowtimeById(id);

        if (showtime.getReservations() != null && !showtime.getReservations().isEmpty()) {
            if (showtime.getReservations().stream().anyMatch(reservation -> reservation.getStatus() != ReservationStatus.CANCELLED)) {
                throw new IllegalStateException("Cannot delete showtime with existing reservations");
            }
        }

        showtimeRepository.delete(showtime);
    }

    private void validateNoScheduleConflict(Long roomId, LocalDateTime startTime, Integer durationInMinutes, Long excludedShowtimeId) {
        LocalDateTime requestedEndTime = startTime.plusMinutes(durationInMinutes.longValue());
        List<Showtime> roomShowtimes = showtimeRepository.findByRoomId(roomId);

        for (Showtime existingShowtime : roomShowtimes) {
            if (excludedShowtimeId != null && excludedShowtimeId.equals(existingShowtime.getId())) {
                continue;
            }

            boolean overlaps = isOverlaps(startTime, existingShowtime, requestedEndTime);

            if (overlaps) {
                throw new IllegalArgumentException(
                        "Showtime overlaps with existing showtime " + existingShowtime.getId() + " in room " + roomId
                );
            }
        }
    }

    private static boolean isOverlaps(LocalDateTime startTime, Showtime existingShowtime, LocalDateTime requestedEndTime) {
        if (existingShowtime.getMovie() == null || existingShowtime.getMovie().getDurationInMinutes() == null) {
            throw new IllegalStateException("Existing showtime is missing movie duration: " + existingShowtime.getId());
        }

        LocalDateTime existingStartTime = existingShowtime.getStartTime();
        LocalDateTime existingEndTime = existingStartTime.plusMinutes(existingShowtime.getMovie().getDurationInMinutes().longValue());
        return startTime.isBefore(existingEndTime) && existingStartTime.isBefore(requestedEndTime);
    }
}
