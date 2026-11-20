package com.hugobumba.cinemasystem.room;

import com.hugobumba.cinemasystem.cinema.Cinema;
import com.hugobumba.cinemasystem.room.dtos.RoomRequestDTO;
import com.hugobumba.cinemasystem.room.dtos.RoomResponseDTO;
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
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping
    public RoomResponseDTO createRoom(@RequestBody @Valid RoomRequestDTO request) {
        Room room = toEntity(request);
        Room createdRoom = roomService.createRoom(room);
        return toResponse(createdRoom);
    }

    @GetMapping("/{id}")
    public RoomResponseDTO getRoomById(@PathVariable Long id) {
        Room room = roomService.getRoomById(id);
        return toResponse(room);
    }

    @GetMapping("/cinema/{cinemaId}")
    public List<RoomResponseDTO> listRoomsByCinema(@PathVariable Long cinemaId) {
        List<Room> rooms = roomService.listRoomsByCinemaId(cinemaId);
        return rooms.stream().map(this::toResponse).toList();
    }

    @GetMapping
    public List<RoomResponseDTO> listRooms(@RequestParam(required = false) Long cinemaId) {
        List<Room> rooms = cinemaId == null
                ? roomService.listAllRooms()
                : roomService.listRoomsByCinemaId(cinemaId);
        return rooms.stream().map(this::toResponse).toList();
    }

    @PutMapping("/{id}")
    public RoomResponseDTO updateRoom(@PathVariable Long id, @RequestBody @Valid RoomRequestDTO request) {
        Room room = toEntity(request);
        room.setId(id);
        Room updatedRoom = roomService.updateRoom(room);
        return toResponse(updatedRoom);
    }

    @DeleteMapping("/{id}")
    public void deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
    }

    private Room toEntity(RoomRequestDTO request) {
        Room room = new Room();
        room.setNumber(request.getNumber());
        Cinema cinema = new Cinema();
        cinema.setId(request.getCinemaId());
        room.setCinema(cinema);
        return room;
    }

    private RoomResponseDTO toResponse(Room room) {
        RoomResponseDTO response = new RoomResponseDTO();
        response.setId(room.getId());
        response.setNumber(room.getNumber());
        response.setCinemaId(room.getCinema().getId());
        return response;
    }
}
