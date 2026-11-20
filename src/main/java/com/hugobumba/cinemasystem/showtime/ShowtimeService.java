package com.hugobumba.cinemasystem.showtime;

import com.hugobumba.cinemasystem.movie.Movie;
import com.hugobumba.cinemasystem.movie.MovieRepository;
import com.hugobumba.cinemasystem.reservation.ReservationStatus;
import com.hugobumba.cinemasystem.room.Room;
import com.hugobumba.cinemasystem.room.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        if (showtime.getId() == null) {
            throw new IllegalArgumentException("Showtime id is required");
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
        if (showtime.getPrice() == null || showtime.getPrice() <= 0) {
            throw new IllegalArgumentException("Showtime price must be greater than zero");
        }
        if (showtimeRepository.existsById(showtime.getId())) {
            throw new IllegalArgumentException("Showtime already exists: " + showtime.getId());
        }
        Room room = roomRepository.findById(showtime.getRoom().getId())
                .orElseThrow(() -> new NoSuchElementException("Room not found: " + showtime.getRoom().getId()));
        Movie movie = movieRepository.findById(showtime.getMovie().getId())
                .orElseThrow(() -> new NoSuchElementException("Movie not found: " + showtime.getMovie().getId()));
        if(showtimeRepository.existsByRoomIdAndStartTime(room.getId(), showtime.getStartTime())) {
            throw new IllegalArgumentException("Showtime already exists for room " + room.getId() + " at " + showtime.getStartTime());
        }

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
        if (showtime.getPrice() == null || showtime.getPrice() <= 0) {
            throw new IllegalArgumentException("Showtime price must be greater than zero");
        }
        Room room = roomRepository.findById(showtime.getRoom().getId())
                .orElseThrow(() -> new NoSuchElementException("Room not found: " + showtime.getRoom().getId()));
        Movie movie = movieRepository.findById(showtime.getMovie().getId())
                .orElseThrow(() -> new NoSuchElementException("Movie not found: " + showtime.getMovie().getId()));
        if (showtimeRepository.existsByRoomIdAndStartTimeAndIdNot(room.getId(), showtime.getStartTime(), showtime.getId())) {
            throw new IllegalArgumentException("Showtime already exists for room " + room.getId() + " at " + showtime.getStartTime());
        }

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
}
