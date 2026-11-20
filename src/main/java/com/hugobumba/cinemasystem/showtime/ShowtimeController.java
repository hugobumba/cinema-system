package com.hugobumba.cinemasystem.showtime;

import com.hugobumba.cinemasystem.movie.Movie;
import com.hugobumba.cinemasystem.room.Room;
import com.hugobumba.cinemasystem.showtime.dtos.ShowtimeRequestDTO;
import com.hugobumba.cinemasystem.showtime.dtos.ShowtimeResponseDTO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/showtimes")
public class ShowtimeController {

    private final ShowtimeService showtimeService;

    public ShowtimeController(ShowtimeService showtimeService) {
        this.showtimeService = showtimeService;
    }

    @PostMapping
    public ShowtimeResponseDTO createShowtime(@RequestBody @Valid ShowtimeRequestDTO request) {
        Showtime showtime = toEntity(request);
        Showtime createdShowtime = showtimeService.createShowtime(showtime);
        return toResponse(createdShowtime);
    }

    @GetMapping("/{id}")
    public ShowtimeResponseDTO getShowtimeById(@PathVariable Long id) {
        Showtime showtime = showtimeService.getShowtimeById(id);
        return toResponse(showtime);
    }

    @GetMapping
    public List<ShowtimeResponseDTO> listShowtimes(
            @RequestParam(required = false) Long movieId,
            @RequestParam(required = false) Long roomId
    ) {
        List<Showtime> showtimes;
        if (movieId != null) {
            showtimes = showtimeService.listShowtimesByMovie(movieId);
        } else if (roomId != null) {
            showtimes = showtimeService.listShowtimesByRoom(roomId);
        } else {
            showtimes = showtimeService.listAllShowtimes();
        }
        return showtimes.stream().map(this::toResponse).toList();
    }

    @PutMapping("/{id}")
    public ShowtimeResponseDTO updateShowtime(@PathVariable Long id, @RequestBody @Valid ShowtimeRequestDTO request) {
        Showtime showtime = toEntity(request);
        showtime.setId(id);
        Showtime updatedShowtime = showtimeService.updateShowtime(showtime);
        return toResponse(updatedShowtime);
    }

    @DeleteMapping("/{id}")
    public void deleteShowtime(@PathVariable Long id) {
        showtimeService.deleteShowtime(id);
    }

    private Showtime toEntity(ShowtimeRequestDTO request) {
        Showtime showtime = new Showtime();
        Room room = new Room();
        room.setId(request.getRoomId());
        showtime.setRoom(room);
        Movie movie = new Movie();
        movie.setId(request.getMovieId());
        showtime.setMovie(movie);
        showtime.setStartTime(request.getStartTime());
        showtime.setPrice(request.getPrice());
        return showtime;
    }

    private ShowtimeResponseDTO toResponse(Showtime showtime) {
        ShowtimeResponseDTO response = new ShowtimeResponseDTO();
        response.setId(showtime.getId());
        response.setRoomId(showtime.getRoom().getId());
        response.setMovieId(showtime.getMovie().getId());
        response.setStartTime(showtime.getStartTime());
        response.setPrice(showtime.getPrice());
        return response;
    }
}
