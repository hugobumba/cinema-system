package com.hugobumba.cinemasystem.seat;

import com.hugobumba.cinemasystem.room.Room;
import com.hugobumba.cinemasystem.seat.dtos.SeatRequestDTO;
import com.hugobumba.cinemasystem.seat.dtos.SeatResponseDTO;
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
@RequestMapping("/api/seats")
public class SeatController {

    private final SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    @PostMapping
    public SeatResponseDTO createSeat(@RequestBody @Valid SeatRequestDTO request) {
        Seat seat = toEntity(request);
        Seat createdSeat = seatService.createSeat(seat);
        return toResponse(createdSeat);
    }

    @GetMapping("/{id}")
    public SeatResponseDTO getSeatById(@PathVariable Long id) {
        Seat seat = seatService.getSeatById(id);
        return toResponse(seat);
    }

    @GetMapping("/available/{showtimeId}")
    public List<SeatResponseDTO> listAvailableSeats(@PathVariable Long showtimeId) {
        List<Seat> availableSeats = seatService.listAvailableSeats(showtimeId);
        return availableSeats.stream().map(this::toResponse).toList();
    }

    @GetMapping
    public List<SeatResponseDTO> listSeats(@RequestParam(required = false) Long roomId) {
        List<Seat> seats = roomId == null
                ? seatService.listAllSeats()
                : seatService.listSeatsByRoom(roomId);
        return seats.stream().map(this::toResponse).toList();
    }

    @PutMapping("/{id}")
    public SeatResponseDTO updateSeat(@PathVariable Long id, @RequestBody @Valid SeatRequestDTO request) {
        Seat seat = toEntity(request);
        seat.setId(id);
        Seat updatedSeat = seatService.updateSeat(seat);
        return toResponse(updatedSeat);
    }

    @DeleteMapping("/{id}")
    public void deleteSeat(@PathVariable Long id) {
        seatService.deleteSeat(id);
    }

    private Seat toEntity(SeatRequestDTO request) {
        Seat seat = new Seat();
        Room room = new Room();
        room.setId(request.getRoomId());
        seat.setRoom(room);
        seat.setSeatRow(request.getSeatRow());
        seat.setSeatNumber(request.getSeatNumber());
        return seat;
    }

    private SeatResponseDTO toResponse(Seat seat) {
        SeatResponseDTO response = new SeatResponseDTO();
        response.setId(seat.getId());
        response.setRoomId(seat.getRoom().getId());
        response.setSeatRow(seat.getSeatRow());
        response.setSeatNumber(seat.getSeatNumber());
        return response;
    }
}
